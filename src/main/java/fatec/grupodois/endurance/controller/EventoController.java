package fatec.grupodois.endurance.controller;

import fatec.grupodois.endurance.entity.Evento;
import fatec.grupodois.endurance.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventoController {


    private final EventoService eventoService;

    @Autowired
    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @PostMapping("/eventos")
    public Evento saveEvento(@RequestBody Evento evento) {
        return eventoService.saveEvento(evento);
    }
}
