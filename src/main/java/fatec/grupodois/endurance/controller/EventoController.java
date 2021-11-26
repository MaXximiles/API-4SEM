package fatec.grupodois.endurance.controller;

import fatec.grupodois.endurance.entity.Evento;
import fatec.grupodois.endurance.entity.Fornecedor;
import fatec.grupodois.endurance.entity.User;
import fatec.grupodois.endurance.enumeration.StatusEvento;
import fatec.grupodois.endurance.exception.*;
import fatec.grupodois.endurance.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

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


        return new ResponseEntity<>(event, CREATED);
    }

    @PutMapping("/add-guest/{id}")
    public ResponseEntity<Evento> addParticipante(@RequestBody User user, @PathVariable("id") Long id)
            throws EventoNotFoundException, EventoFullException, UserIsNotActiveException, UserJaCadastradoNoEventoException {

        Evento event = eventoService.addParticipante(user, id);

        return new ResponseEntity<>(event, OK);
    }

    @PutMapping("/add-fornecedor/{id}")
    public ResponseEntity<Evento> addParticipante(@RequestParam Fornecedor fornecedor, @PathVariable("id") Long id)
            throws EventoNotFoundException, FornecedorJaCadastradoNoEventoException {

        Evento event = eventoService.addFornecedor(fornecedor, id);

        return new ResponseEntity<>(event, OK);
    }

    @PutMapping("/remove-guest/{id}")
    public ResponseEntity<Evento> removeParticipante(@RequestBody User user, @PathVariable("id") Long id)
            throws EventoNotFoundException {

        Evento event = eventoService.removeParticipante(user, id);

        return new ResponseEntity<>(event, OK);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<?> deleteEventoById(@PathVariable("id") Long eventoId)
                                                throws EventoNotFoundException {
        eventoService.deleteEventoById(eventoId);

        return new ResponseEntity<>(OK);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<Evento>> fetchEventoList() {
        List<Evento> eventos = eventoService.findAllEventos();

        return new ResponseEntity<>(eventos, OK);
    }

    @GetMapping(path = "/get-participantes")
    public ResponseEntity<List<User>> fetchParticipantes(@RequestBody Evento event) {
        List<User> participantes = eventoService.getParticipantes(event);

        return new ResponseEntity<>(participantes, OK);
    }

    @GetMapping("/fetch/{id}")
    public ResponseEntity<Evento> fetchEventoById(@PathVariable("id") Long eventoId)
                                                    throws EventoNotFoundException {
        Evento evento = eventoService.fetchEventoById(eventoId);
        return new ResponseEntity<>(evento, CREATED);
    }

    @GetMapping(path = "/all/status/{eventoStatus}")
    public ResponseEntity<List<Evento>> fetchEventoListByStatus(@PathVariable("eventoStatus")StatusEvento status) throws EventoNotFoundException {
        List<Evento> eventos = eventoService.findEventoByStatus(status);

        return new ResponseEntity<>(eventos, OK);
    }

    @GetMapping(path = "/all/date-time/{eventoInicio}")
    public ResponseEntity<List<Evento>> fetchEventoListByDateTime(@PathVariable("eventoInicio")
                                                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                                          LocalDateTime date) throws EventoNotFoundException {
        List<Evento> eventos = eventoService.findEventoByDateTime(date);

        return new ResponseEntity<>(eventos, OK);
    }

    @GetMapping(path = "/all/date/{eventoInicio}")
    public ResponseEntity<List<Evento>> fetchEventoListByDate(@PathVariable("eventoInicio")
                                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                                      LocalDate date) throws EventoNotFoundException {
        List<Evento> eventos = eventoService.findEventoByDate(date);

        return new ResponseEntity<>(eventos, OK);
    }

    @PutMapping(path = "/update/{eventoId}")
    public ResponseEntity<Evento> updateEvento(@PathVariable("eventoId") Long eventoId,
                                               @RequestBody Evento evento)
            throws EventoNotFoundException, EventoInicioAfterException,
            EventIsOccurringException, EventOutOfOpeningHoursException,
            EventoInicioExistException, EventDifferentDayException, MessagingException {

        eventoService.updateEvento(eventoId, evento);
        return new ResponseEntity<>(evento, OK);
    }

}
