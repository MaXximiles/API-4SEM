package fatec.grupodois.endurance.service.impl;

import fatec.grupodois.endurance.entity.Evento;
import fatec.grupodois.endurance.entity.StatusEvento;
import fatec.grupodois.endurance.exception.EventoInicioAfterException;
import fatec.grupodois.endurance.exception.EventoNotFoundException;
import fatec.grupodois.endurance.repository.EventoRepository;
import fatec.grupodois.endurance.service.EventoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EventoServiceImpl implements EventoService {

    private EventoRepository eventoRepository;

    public EventoServiceImpl(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    public Evento addEvento(Evento evento) throws EventoInicioAfterException {
        
        if(evento.getEventoInicio().isAfter(evento.getEventoFim())) {
            throw new EventoInicioAfterException(evento.getEventoInicio()
                                                    + " depois de "
                                                    + evento.getEventoFim());
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
    public List<Evento> findEventoByStatus(StatusEvento status) {
        return eventoRepository.findEventoByStatus(status).get();
    }

    @Override
    public List<Evento> findEventoByDateTime(LocalDateTime date) {

        return eventoRepository.findEventoByDateTime(date).get();
    }

    @Override
    public List<Evento> findEventoByDate(LocalDate date) {

        return eventoRepository.findEventoByDate(date).get();
    }

    @Override
    public Evento updateEvento(Long eventoId, Evento evento) {

        Evento eventoDb = eventoRepository.findById(eventoId).get();

        if(StringUtils.isNotEmpty(StringUtils.trim(evento.getEventoTema())) &&
                !StringUtils.equalsIgnoreCase(evento.getEventoTema(), eventoDb.getEventoTema())) {

            eventoDb.setEventoTema(evento.getEventoTema());
        }

        if(Objects.nonNull(evento.getEventoInicio()) &&
            !eventoDb.getEventoInicio().equals(evento.getEventoInicio())) {
            eventoDb.setEventoInicio(evento.getEventoInicio());
        }

        if(Objects.nonNull(evento.getEventoFim()) &&
                !eventoDb.getEventoFim().equals(evento.getEventoFim())) {
            eventoDb.setEventoFim(evento.getEventoFim());
        }

        if(StringUtils.isNotEmpty(StringUtils.trim(evento.getEventoLocal())) &&
                !StringUtils.equalsIgnoreCase(evento.getEventoLocal(), eventoDb.getEventoLocal())) {
            eventoDb.setEventoLocal(evento.getEventoLocal());
        }

        if(StringUtils.isNotEmpty(StringUtils.trim(evento.getEventoObservacao())) &&
                !StringUtils.equalsIgnoreCase(evento.getEventoObservacao(), eventoDb.getEventoObservacao())) {
            eventoDb.setEventoObservacao(evento.getEventoObservacao());
        }

        if(Objects.nonNull(evento.getEventoStatus()) &&
                evento.getEventoStatus() != eventoDb.getEventoStatus()) {
            eventoDb.setEventoStatus(evento.getEventoStatus());
        }
        

        return eventoRepository.save(eventoDb);
    }
}
