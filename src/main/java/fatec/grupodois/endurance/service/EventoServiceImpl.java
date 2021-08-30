package fatec.grupodois.endurance.service;

import fatec.grupodois.endurance.entity.Evento;
import fatec.grupodois.endurance.entity.StatusEvento;
import fatec.grupodois.endurance.repository.EventoRepository;
import org.springframework.stereotype.Service;

@Service
public class EventoServiceImpl implements EventoService{

    private EventoRepository eventoRepository;

    public EventoServiceImpl(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    public Evento saveEvento(Evento evento) {

        return eventoRepository.save(evento);
    }
}
