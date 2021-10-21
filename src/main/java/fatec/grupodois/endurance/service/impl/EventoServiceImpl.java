package fatec.grupodois.endurance.service.impl;

import fatec.grupodois.endurance.entity.Evento;
import fatec.grupodois.endurance.entity.User;
import fatec.grupodois.endurance.enumeration.StatusEvento;
import fatec.grupodois.endurance.exception.*;
import fatec.grupodois.endurance.repository.EventoRepository;
import fatec.grupodois.endurance.repository.UserRepository;
import fatec.grupodois.endurance.service.EmailService;
import fatec.grupodois.endurance.service.EventoService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static fatec.grupodois.endurance.constant.EventoImplConstant.*;

@Service
public class EventoServiceImpl implements EventoService {

    private final EventoRepository eventoRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());


    @Autowired
    public EventoServiceImpl(EventoRepository eventoRepository, EmailService emailService, UserRepository userRepository) {
        this.eventoRepository = eventoRepository;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }


    public Evento addEvento(Evento evento)
            throws EventoInicioAfterException, EventoInicioExistException,
            EventIsOccurringException, EventOutOfOpeningHoursException,
            MessagingException, EventDifferentDayException, EventWithInvalidLocalException {

        checkEventIntegrity(evento.getInicio(), evento.getFim(), evento.getLocal(), evento.getLocal());

        if(!evento.getLocal().equalsIgnoreCase("OPENSPACE") &&
                !evento.getLocal().equalsIgnoreCase("LOUNGE")) {

            throw new EventWithInvalidLocalException(EVENT_INVALID_LOCAL);
        }

        evento.setMaxParticipantes(evento.getLocal().equals("OPENSPACE")? 50:10);
        evento.setCriacao(LocalDateTime.now());
        evento.setTotalParticipantes(0);
        evento.setParticipantes(new ArrayList<>());

        if(evento.getUser().getRole().equals("ROLE_ADMIN")) {
            evento.setStatus("CONFIRMADO");
        } else {
            List<User> adminUsers = userRepository.findAllAdmins();
            for(User u: adminUsers) {
                emailService.sendNewEventEmail(u.getFirstName(), evento.getTema(), u.getEmail());
            }
        }

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
            EventOutOfOpeningHoursException, EventoInicioExistException, EventDifferentDayException {

        Evento eventoDb = eventoRepository.findById(eventoId).get();


        if(StringUtils.isNotEmpty(StringUtils.trim(evento.getTema())) &&
                !StringUtils.equalsIgnoreCase(evento.getTema(), eventoDb.getTema())) {

            eventoDb.setTema(evento.getTema());
        }

        if(Objects.nonNull(evento.getInicio()) &&
            !eventoDb.getInicio().equals(evento.getInicio())) {
            checkEventIntegrity(evento.getInicio(), evento.getFim(), evento.getLocal(), evento.getTema());
            eventoDb.setInicio(evento.getInicio());
        }

        if(Objects.nonNull(evento.getFim()) &&
                !eventoDb.getFim().equals(evento.getFim())) {
            checkEventIntegrity(evento.getInicio(), evento.getFim(), evento.getLocal(), evento.getTema());
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
        // TODO checar status do user
        Evento event = fetchEventoById(id);

        boolean flag = event.addParticipante(user);

        if(flag) {
            return eventoRepository.save(event);
        } else {
            throw new EventoFullException(EVENT_IS_FULL);
        }

    }

    private void checkEventIntegrity(LocalDateTime inicio, LocalDateTime fim, String local, String tema) throws EventoInicioAfterException, EventOutOfOpeningHoursException,
            EventoInicioExistException, EventIsOccurringException, EventDifferentDayException {
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

        if(inicio.getDayOfMonth() != fim.getDayOfMonth()) {
            throw new EventDifferentDayException(EVENT_DIFFERENT_DAY);
        }

        LocalDate date = inicio.toLocalDate();
        Optional<List<Evento>> eventos = eventoRepository.findEventoByDate(date);
        List<LocalTime> horasDisp = getDaySchedule(eventos.get());

        if (eventos.isPresent()) {
            for (Evento s : eventos.get()) {
                if (local.equalsIgnoreCase(s.getLocal()) && !tema.equalsIgnoreCase(s.getTema())) {
                    if (s.getInicio().toLocalTime().equals(inicio.toLocalTime())) {

                        throw new EventoInicioExistException(EVENT_BEGIN_EXISTS
                                + inicio.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")) +
                                SUGGESTION + horasDisp.get(0));
                    } else if (inicio.toLocalTime().isAfter(s.getInicio().toLocalTime()) &&
                    inicio.toLocalTime().isBefore(s.getFim().toLocalTime())) {
                        throw new EventIsOccurringException(EVENT_IS_OCCURRING
                                + inicio.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")) +
                                SUGGESTION + horasDisp.get(0));
                    } else if (fim.toLocalTime().isBefore(s.getFim().toLocalTime()) &&
                                fim.toLocalTime().isAfter(s.getInicio().toLocalTime())) {
                        throw new EventIsOccurringException(EVENT_IS_OCCURRING
                                + fim.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                                + SUGGESTION + horasDisp.get(0));
                    }
                }

            }
        }
    }

    private List<LocalTime> getDaySchedule(List<Evento> eventos) {

        List<String> horasDisp = new ArrayList<>();
        horasDisp.add("08:00");
        horasDisp.add("09:00");
        horasDisp.add("10:00");
        horasDisp.add("11:00");
        horasDisp.add("12:00");
        horasDisp.add("13:00");
        horasDisp.add("14:00");
        horasDisp.add("15:00");
        horasDisp.add("16:00");
        horasDisp.add("17:00");
        horasDisp.add("18:00");
        horasDisp.add("19:00");
        horasDisp.add("20:00");
        horasDisp.add("21:00");
        horasDisp.add("22:00");

        List<LocalTime> horasDispAns = new ArrayList<>();

        for(int i=0;i<eventos.size();i++) {
            String time = eventos.get(i).getInicio().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
            String time2 = eventos.get(i).getFim().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
            for(int j=0;j<horasDisp.size();j++) {
                if(time.equals(horasDisp.get(j)) || time2.equals(horasDisp.get(j))) {
                    horasDisp.remove(j);
                }
            }
        }

        for(String s: horasDisp) {
            String parts[] = s.split(":");
            int h = Integer.valueOf(parts[0]);
            int m = Integer.valueOf(parts[1]);
            horasDispAns.add(LocalTime.of(h, m));
        }

        LOGGER.info(String.valueOf(horasDispAns));

        return horasDispAns;
    }

}
