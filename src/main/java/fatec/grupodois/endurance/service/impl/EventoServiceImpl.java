package fatec.grupodois.endurance.service.impl;

import fatec.grupodois.endurance.entity.Evento;
import fatec.grupodois.endurance.entity.User;
import fatec.grupodois.endurance.enumeration.StatusEvento;
import fatec.grupodois.endurance.exception.*;
import fatec.grupodois.endurance.repository.EventoRepository;
import fatec.grupodois.endurance.service.EventoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static fatec.grupodois.endurance.constant.EventoImplConstant.*;

@Service
public class EventoServiceImpl implements EventoService {

    private final EventoRepository eventoRepository;


    @Autowired
    public EventoServiceImpl(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }


    public Evento addEvento(Evento evento) throws EventoInicioAfterException, EventoInicioExistException, EventIsOccurringException, EventOutOfOpeningHoursException {

       checkEventIntegrity(evento.getInicio(), evento.getFim(), evento.getLocal());

        Arrays.stream(StatusEvento.values()).forEach(statusEvento -> {
            statusEvento.name().equals(evento.getStatus().toUpperCase());
        });

        evento.setMaxParticipantes(evento.getLocal().equals("Openspace")? 50:10);
        evento.setCriacao(LocalDateTime.now());
        evento.setTotalParticipantes(0);
        evento.setParticipantes(new ArrayList<>());

        return eventoRepository.save(evento);
    }

    @Override
    public void deleteEventoById(Long eventoId) throws EventoNotFoundException {

        Optional<Evento> eventoOptional = eventoRepository
                .findById(eventoId);

        if(eventoOptional.isEmpty()) {
            throw new EventoNotFoundException("Evento "
                    + "com ID "
                    + eventoId
                    + " não encontrado.");
        }

        eventoRepository.deleteById(eventoId);
    }

    @Override
    public List<Evento> findAllEventos() {
        return eventoRepository.findAll();
    }

    @Override
    public Evento fetchEventoById(Long eventoId) throws EventoNotFoundException {

        Optional<Evento> eventoOptional = eventoRepository
                .findById(eventoId);

        if(eventoOptional.isEmpty()) {
            throw new EventoNotFoundException("Evento "
            + "com ID "
            + eventoId
            + " não encontrado.");
        }

        return eventoRepository.findById(eventoId).get();
    }

    @Override
    public List<Evento> findEventoByStatus(StatusEvento status) throws EventoNotFoundException {

        Optional<List<Evento>> eventos = eventoRepository.findEventoByStatus(status);

        if(eventos.isEmpty()) {
            throw new EventoNotFoundException("Evento com status " + status.name() + " não encontrado");
        }

        return eventos.get();
    }

    @Override
    public List<Evento> findEventoByDateTime(LocalDateTime date) throws EventoNotFoundException {

        Optional<List<Evento>> eventos = eventoRepository.findEventoByDateTime(date);

        if(eventos.isEmpty()) {
            throw new EventoNotFoundException("Evento com data: "
                    + date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"))
                    + " não encontrado");
        }

        return eventos.get();
    }

    @Override
    public List<Evento> findEventoByDate(LocalDate date) throws EventoNotFoundException {

        Optional<List<Evento>> eventos = eventoRepository.findEventoByDate(date);

        if(eventos.isEmpty()) {
            throw new EventoNotFoundException("Evento com data: "
                    + date.format(DateTimeFormatter.ISO_LOCAL_DATE)
                    + " não encontrado");
        }

        return eventos.get();
    }

    @Override
    public Evento updateEvento(Long eventoId, Evento evento)
            throws EventoInicioAfterException, EventIsOccurringException,
            EventOutOfOpeningHoursException, EventoInicioExistException {

        Evento eventoDb = eventoRepository.findById(eventoId).get();

        checkEventIntegrity(evento.getInicio(), evento.getFim(), evento.getLocal());

        if(StringUtils.isNotEmpty(StringUtils.trim(evento.getTema())) &&
                !StringUtils.equalsIgnoreCase(evento.getTema(), eventoDb.getTema())) {

            eventoDb.setTema(evento.getTema());
        }

        if(Objects.nonNull(evento.getInicio()) &&
            !eventoDb.getInicio().equals(evento.getInicio())) {
            checkEventIntegrity(evento.getInicio(), evento.getFim(), evento.getLocal());
            eventoDb.setInicio(evento.getInicio());
        }

        if(Objects.nonNull(evento.getFim()) &&
                !eventoDb.getFim().equals(evento.getFim())) {
            checkEventIntegrity(evento.getInicio(), evento.getFim(), evento.getLocal());
            eventoDb.setFim(evento.getFim());
        }

        if(StringUtils.isNotEmpty(StringUtils.trim(evento.getLocal())) &&
                !StringUtils.equalsIgnoreCase(evento.getLocal(), eventoDb.getLocal())) {
            eventoDb.setLocal(evento.getLocal());
        }

        if(StringUtils.isNotEmpty(StringUtils.trim(evento.getObservacao())) &&
                !StringUtils.equalsIgnoreCase(evento.getObservacao(), eventoDb.getObservacao())) {
            eventoDb.setObservacao(evento.getObservacao());
        }

        if(StringUtils.isNotEmpty(StringUtils.trim(evento.getDescricao())) &&
                !StringUtils.equalsIgnoreCase(evento.getDescricao(), eventoDb.getDescricao())) {
            eventoDb.setDescricao(evento.getDescricao());
        }

        if(Objects.nonNull(evento.getStatus()) &&
                evento.getStatus() != eventoDb.getStatus()) {
            eventoDb.setStatus(evento.getStatus());
        }


        return eventoRepository.save(eventoDb);
    }

    @Override
    public Evento addParticipante(User user, Long id) throws EventoNotFoundException, EventoFullException {

        Evento event = fetchEventoById(id);

        boolean flag = event.addParticipante(user);

        if(flag) {
            return eventoRepository.save(event);
        } else {
            throw new EventoFullException(EVENT_IS_FULL);
        }

    }

    private void checkEventIntegrity(LocalDateTime inicio, LocalDateTime fim, String local) throws EventoInicioAfterException, EventOutOfOpeningHoursException,
            EventoInicioExistException, EventIsOccurringException {
        if(inicio.isAfter(fim)) {
            throw new EventoInicioAfterException(inicio
                    + " depois de "
                    + fim);
        }
        LocalTime open = LocalTime.of(8,00,00);
        LocalTime close = LocalTime.of(22,00,00);

        if(inicio.toLocalTime().isBefore(open)
                || fim.toLocalTime().isAfter(close)
                || inicio.toLocalTime().isAfter(close)
                || fim.toLocalTime().isBefore(open)) {
            throw new EventOutOfOpeningHoursException(EVENT_IS_OUT_OF_OPENING_HOURS);
        }

        LocalDate date = inicio.toLocalDate();

        Optional<List<Evento>> eventos = eventoRepository.findEventoByDate(date);


        if (eventos.isPresent()) {
            for (Evento s : eventos.get()) {
                System.out.println(s.getInicio());
                System.out.println(inicio);
                if (local.equalsIgnoreCase(s.getLocal())) {
                    if (s.getInicio().toLocalTime().equals(inicio.toLocalTime())) {
                        throw new EventoInicioExistException(EVENT_BEGIN_EXISTS
                                + inicio.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
                    } else if (inicio.toLocalTime().isAfter(s.getInicio().toLocalTime()) &&
                    inicio.toLocalTime().isBefore(s.getFim().toLocalTime())) {
                        throw new EventIsOccurringException(EVENT_IS_OCCURRING
                                + inicio.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
                    } else if (fim.toLocalTime().isBefore(s.getFim().toLocalTime()) &&
                                fim.toLocalTime().isAfter(s.getInicio().toLocalTime())) {
                        throw new EventIsOccurringException(EVENT_IS_OCCURRING
                                + fim.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
                    }
                }

            }
        }
    }
}
