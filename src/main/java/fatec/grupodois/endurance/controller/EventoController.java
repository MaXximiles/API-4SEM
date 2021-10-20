package fatec.grupodois.endurance.controller;

import fatec.grupodois.endurance.entity.Evento;
import fatec.grupodois.endurance.entity.User;
import fatec.grupodois.endurance.enumeration.StatusEvento;
import fatec.grupodois.endurance.exception.*;
import fatec.grupodois.endurance.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventoController extends ExceptionHandling{


    private final EventoService eventoService;

    @Autowired
    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @PostMapping("/add")
    public ResponseEntity<Evento> addEvento(@RequestBody Evento evento)
            throws EventoInicioAfterException, EventIsOccurringException,
            EventoInicioExistException, EventOutOfOpeningHoursException,
            MessagingException, EventDifferentDayException,
            EventWithInvalidLocalException {

        Evento event = eventoService.addEvento(evento);


        return new ResponseEntity<>(event, HttpStatus.CREATED);
    }

    @PutMapping("/add-guest/{id}")
    public ResponseEntity<Evento> addParticipante(@RequestBody User user, @PathVariable("id") Long id)
            throws EventoNotFoundException, EventoFullException {

        Evento event = eventoService.addParticipante(user, id);

        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<?> deleteEventoById(@PathVariable("id") Long eventoId)
                                                throws EventoNotFoundException {
        eventoService.deleteEventoById(eventoId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<Evento>> fetchEventoList() {
        List<Evento> eventos = eventoService.findAllEventos();

        return new ResponseEntity<>(eventos, HttpStatus.OK);
    }

    @GetMapping("/fetch/{id}")
    public ResponseEntity<Evento> fetchEventoById(@PathVariable("id") Long eventoId)
                                                    throws EventoNotFoundException {
        Evento evento = eventoService.fetchEventoById(eventoId);
        return new ResponseEntity<>(evento, HttpStatus.CREATED);
    }

    @GetMapping(path = "/all/status/{eventoStatus}")
    public ResponseEntity<List<Evento>> fetchEventoListByStatus(@PathVariable("eventoStatus")StatusEvento status) throws EventoNotFoundException {
        List<Evento> eventos = eventoService.findEventoByStatus(status);

        return new ResponseEntity<>(eventos, HttpStatus.OK);
    }

    @GetMapping(path = "/all/date-time/{eventoInicio}")
    public ResponseEntity<List<Evento>> fetchEventoListByDateTime(@PathVariable("eventoInicio")
                                                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                                          LocalDateTime date) throws EventoNotFoundException {
        List<Evento> eventos = eventoService.findEventoByDateTime(date);

        return new ResponseEntity<>(eventos, HttpStatus.OK);
    }

    @GetMapping(path = "/all/date/{eventoInicio}")
    public ResponseEntity<List<Evento>> fetchEventoListByDate(@PathVariable("eventoInicio")
                                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                                      LocalDate date) throws EventoNotFoundException {
        List<Evento> eventos = eventoService.findEventoByDate(date);

        return new ResponseEntity<>(eventos, HttpStatus.OK);
    }

    @PutMapping(path = "/update/{eventoId}")
    public ResponseEntity<Evento> updateEvento(@PathVariable("eventoId") Long eventoId,
                                               @RequestBody Evento evento)
            throws EventoNotFoundException, EventoInicioAfterException,
            EventIsOccurringException, EventOutOfOpeningHoursException,
            EventoInicioExistException, EventDifferentDayException {

        eventoService.updateEvento(eventoId, evento);
        return new ResponseEntity<>(evento, HttpStatus.OK);
    }

}
