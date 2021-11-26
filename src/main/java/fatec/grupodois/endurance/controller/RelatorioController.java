package fatec.grupodois.endurance.controller;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.text.Document;
import fatec.grupodois.endurance.DBConexao;
import fatec.grupodois.endurance.exception.ExceptionHandling;
import fatec.grupodois.endurance.exception.NenhumResultadoException;
import fatec.grupodois.endurance.service.RelatorioService;
import fatec.grupodois.endurance.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;


@RestController
@RequestMapping("/relatorios")
public class RelatorioController extends ExceptionHandling {

    private final RelatorioService service;

    public RelatorioController(RelatorioService relatorioService) {
        this.service = relatorioService;
    }

    /* Relatório de eventos por periodo */
    @GetMapping(path = "/eventos_periodo/{dataInicio},{dataFim}")
    public ResponseEntity<byte[]> fetchRelatorioPeriodo(@PathVariable(value = "dataInicio", required = false) String dataInicio,
                       @PathVariable(value= "dataFim", required = false) String dataFim) throws NenhumResultadoException {

        return service.fetchRelatorioPeriodo(dataInicio, dataFim);
    }

    /* Relatório de eventos por usuario */
    @GetMapping(path = "/eventos_usuario/{usuarioId}")
    public  ResponseEntity<byte[]> fetchRelatorioUsuario(@PathVariable(value= "usuarioId", required = false) Long usuarioId)
            throws NenhumResultadoException {
        return service.fetchRelatorioUsuario(usuarioId);
    }

    /* Relatório de usuarios vacina */
    @GetMapping(path = "/eventos_vacina/{vacinados}")
    public ResponseEntity<byte[]> fetchRelatorioVacina(@PathVariable(value= "vacinados", required = false) String vacinados) {
        return service.fetchRelatorioVacinados(vacinados);
    }



}
