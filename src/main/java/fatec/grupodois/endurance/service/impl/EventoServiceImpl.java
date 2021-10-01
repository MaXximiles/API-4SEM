package fatec.grupodois.endurance.service.impl;

import fatec.grupodois.endurance.entity.Evento;
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

@Service
public class EventoServiceImpl implements EventoService {

    private final EventoRepository eventoRepository;


    @Autowired
    public EventoServiceImpl(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }


    public Evento addEvento(Evento evento) throws EventoInicioAfterException, EventoInicioExistException, EventIsOccurringException, EventOutOfOpeningHoursException {

        checkEventIntegrity(evento);

        evento.setMaxParticipantes(evento.getLocal().equals("Openspace")? 50:10);
        evento.setCriacao(LocalDateTime.now());
        evento.setTotalParticipantes(0);

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

        checkEventIntegrity(evento);

        if(StringUtils.isNotEmpty(StringUtils.trim(evento.getTema())) &&
                !StringUtils.equalsIgnoreCase(evento.getTema(), eventoDb.getTema())) {

            eventoDb.setTema(evento.getTema());
        }

        if(Objects.nonNull(evento.getInicio()) &&
            !eventoDb.getInicio().equals(evento.getInicio())) {
            eventoDb.setInicio(evento.getInicio());
        }

        if(Objects.nonNull(evento.getFim()) &&
                !eventoDb.getFim().equals(evento.getFim())) {
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

        if(Objects.nonNull(evento.getStatus()) &&
                !evento.getStatus().equalsIgnoreCase(eventoDb.getStatus())) {
            eventoDb.setStatus(evento.getStatus());
        }
        

        return eventoRepository.save(eventoDb);
    }

    private void checkEventIntegrity(Evento evento) throws EventoInicioAfterException, EventOutOfOpeningHoursException,
            EventoInicioExistException, EventIsOccurringException {
        if(evento.getInicio().isAfter(evento.getFim())) {
            throw new EventoInicioAfterException(evento.getInicio()
                    + " depois de "
                    + evento.getFim());
        }
        LocalTime open = LocalTime.of(8,00,00);
        LocalTime close = LocalTime.of(22,00,00);

        if(evento.getInicio().toLocalTime().isBefore(open)
                || evento.getFim().toLocalTime().isAfter(close)
                || evento.getInicio().toLocalTime().isAfter(close)
                || evento.getFim().toLocalTime().isBefore(open)) {
            throw new EventOutOfOpeningHoursException("Evento fora do horário de funcionamento: 08:00 às 22:00");
        }

        LocalDate date = evento.getInicio().toLocalDate();

        Optional<List<Evento>> eventos = eventoRepository.findEventoByDate(date);

        if (eventos.isPresent()) {
            for (Evento s : eventos.get()) {
                if (evento.getLocal().equals(s.getLocal())) {
                    if (s.getInicio().equals(evento.getInicio())) {
                        throw new EventoInicioExistException("Evento já cadastrado com ínício: "
                                + evento.getInicio().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
                    } else if (evento.getInicio().isAfter(s.getInicio()) &&
                            evento.getInicio().isBefore(s.getFim())) {
                        throw new EventIsOccurringException("Evento ocorrendo no horário de início: "
                                + evento.getInicio().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
                    }
                }

            }
        }
    }
}
