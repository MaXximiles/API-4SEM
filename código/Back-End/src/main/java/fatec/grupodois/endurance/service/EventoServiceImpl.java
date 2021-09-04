package fatec.grupodois.endurance.service;

import fatec.grupodois.endurance.entity.Evento;
import fatec.grupodois.endurance.entity.StatusEvento;
import fatec.grupodois.endurance.error.EventoInicioAfterException;
import fatec.grupodois.endurance.error.EventoNotFoundException;
import fatec.grupodois.endurance.repository.EventoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EventoServiceImpl implements EventoService{

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
    public Evento updateEvento(Long eventoId, Evento evento) throws EventoNotFoundException, EventoInicioAfterException {

        /*Optional<Evento> eventoOptional = eventoRepository
                .findById(eventoId);

        if(eventoOptional.isEmpty()) {
            throw new EventoNotFoundException("Evento "
                    + "com ID "
                    + eventoId
                    + " não encontrado.");
        }*/

        Evento eventoDb = eventoRepository.findById(eventoId).get();

        if(Objects.nonNull(evento.getEventoTema()) &&
                !"".equalsIgnoreCase(evento.getEventoTema())) {
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

        if(Objects.nonNull(evento.getEventoLocal()) &&
                !"".equalsIgnoreCase(evento.getEventoLocal())) {
            eventoDb.setEventoLocal(evento.getEventoLocal());
        }

        if(Objects.nonNull(evento.getEventoObservacao()) &&
                !"".equalsIgnoreCase(evento.getEventoObservacao())) {
            eventoDb.setEventoObservacao(evento.getEventoObservacao());
        }

        if(Objects.nonNull(evento.getEventoStatus()) &&
                evento.getEventoStatus() != eventoDb.getEventoStatus()) {
            eventoDb.setEventoStatus(evento.getEventoStatus());
        }
        

        return eventoRepository.save(eventoDb);
    }
}
