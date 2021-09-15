package fatec.grupodois.endurance.service;

import fatec.grupodois.endurance.entity.Evento;
import fatec.grupodois.endurance.entity.StatusEvento;
import fatec.grupodois.endurance.exception.EventoInicioAfterException;
import fatec.grupodois.endurance.exception.EventoNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface EventoService {

    Evento addEvento(Evento evento) throws EventoInicioAfterException;

    void deleteEventoById(Long eventoId) throws EventoNotFoundException;

    List<Evento> findAllEventos();

    Evento fetchEventoById(Long eventoId) throws EventoNotFoundException;

    List<Evento> findEventoByStatus(StatusEvento status);

    List<Evento> findEventoByDateTime(LocalDateTime date);

    List<Evento> findEventoByDate(LocalDate date);

    Evento updateEvento(Long eventoId, Evento evento) throws EventoNotFoundException, EventoInicioAfterException;

}
