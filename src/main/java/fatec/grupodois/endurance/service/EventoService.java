package fatec.grupodois.endurance.service;

import fatec.grupodois.endurance.entity.Evento;
import fatec.grupodois.endurance.entity.User;
import fatec.grupodois.endurance.enumeration.StatusEvento;
import fatec.grupodois.endurance.exception.*;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface EventoService {

    Evento addEvento(Evento evento) throws EventoInicioAfterException, EventoInicioExistException, EventIsOccurringException, EventOutOfOpeningHoursException, MessagingException, EventDifferentDayException, EventWithInvalidLocalException;

    void deleteEventoById(Long eventoId) throws EventoNotFoundException;

    List<Evento> findAllEventos();

    Evento fetchEventoById(Long eventoId) throws EventoNotFoundException;

    List<Evento> findEventoByStatus(StatusEvento status) throws EventoNotFoundException;

    List<Evento> findEventoByDateTime(LocalDateTime date) throws EventoNotFoundException;

    List<Evento> findEventoByDate(LocalDate date) throws EventoNotFoundException;

    Evento updateEvento(Long eventoId, Evento evento) throws EventoNotFoundException, EventoInicioAfterException, EventIsOccurringException, EventOutOfOpeningHoursException, EventoInicioExistException, EventDifferentDayException;

    Evento addParticipante(User user, Long id) throws EventoNotFoundException, EventoFullException;
}
