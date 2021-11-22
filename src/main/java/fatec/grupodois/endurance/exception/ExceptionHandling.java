package fatec.grupodois.endurance.exception;

import com.auth0.jwt.exceptions.TokenExpiredException;
import fatec.grupodois.endurance.entity.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.Objects;

import static org.springframework.http.HttpStatus.*;


@RestControllerAdvice
public class ExceptionHandling implements ErrorController{

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private static final String ACCOUNT_LOCKED = "Sua conta foi bloqueada. Por favor contate um admnistrador";
    private static final String METHOD_IS_NOT_ALLOWED = "Requisição não disponível. Por favor requisite uma '%s' requisição";
    private static final String INTERNAL_SERVER_ERROR_MSG = "Um erro ocorreu ao processar sua requisição";
    private static final String INCORRECT_CREDENTIALS = "Email / senha incorreto. Por favor tente novamente";
    private static final String ACCOUNT_DISABLED = "Sua conta foi desabilitada. Por favor contate um admnistrador";
    private static final String ERROR_PROCESSING_FILE = "Um erro ocorreu ao processar arquivo";
    private static final String NOT_ENOUGH_PERMISSION = "Usuário não possui permissão necessária";
    public static final String ERROR_PATH = "/error";

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<HttpResponse> accountDisableException() {

        return createHttpResponse(BAD_REQUEST, ACCOUNT_DISABLED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HttpResponse> badCredentialsException() {
        return createHttpResponse(BAD_REQUEST, INCORRECT_CREDENTIALS);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<HttpResponse> accessDeniedException() {
        return createHttpResponse(FORBIDDEN, NOT_ENOUGH_PERMISSION);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<HttpResponse> lockedException() {
        return createHttpResponse(UNAUTHORIZED, ACCOUNT_LOCKED);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<HttpResponse> tokenExpiredException(TokenExpiredException exception) {
        return createHttpResponse(UNAUTHORIZED, exception.getMessage());
    }

    /* Especifico para User */

    @ExceptionHandler(EmailExistException.class)
    public ResponseEntity<HttpResponse> emailExistException(EmailExistException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<HttpResponse> emailNotFoundException(EmailNotFoundException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(CpfExistException.class)
    public ResponseEntity<HttpResponse> cpfExistException(CpfExistException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(CpfNotFoundException.class)
    public ResponseEntity<HttpResponse> cpfNotFoundException(CpfNotFoundException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<HttpResponse> userNotFoundException(UserNotFoundException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }
    /* fim específico para User */


    /* Especifico para Evento */
    @ExceptionHandler(EventIsOccurringException.class)
    public ResponseEntity<HttpResponse> eventIsOccurringException(EventIsOccurringException exc) {
        return createHttpResponse(BAD_REQUEST, exc.getMessage());
    }

    @ExceptionHandler(EventDifferentDayException.class)
    public ResponseEntity<HttpResponse> eventDifferentDay(EventDifferentDayException exc) {
        return createHttpResponse(BAD_REQUEST, exc.getMessage());
    }

    @ExceptionHandler(EventoInicioAfterException.class)
    public ResponseEntity<HttpResponse> eventoInicioAfterException(EventoInicioAfterException exc) {
        return createHttpResponse(BAD_REQUEST, exc.getMessage());
    }

    @ExceptionHandler(EventoInicioExistException.class)
    public ResponseEntity<HttpResponse> eventoInicioExistException(EventoInicioExistException exc) {
        return createHttpResponse(BAD_REQUEST, exc.getMessage());
    }

    @ExceptionHandler(EventoNotFoundException.class)
    public ResponseEntity<HttpResponse> eventoNotFoundException(EventoNotFoundException exc) {
        return createHttpResponse(BAD_REQUEST, exc.getMessage());
    }

    @ExceptionHandler(EventOutOfOpeningHoursException.class)
    public ResponseEntity<HttpResponse> eventOutOfOpeningHoursException(EventOutOfOpeningHoursException exc) {
        return createHttpResponse(BAD_REQUEST, exc.getMessage());
    }
    /* fim específico para Evento */

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<HttpResponse> methodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        HttpMethod supportedMethod = Objects.requireNonNull(exception.getSupportedHttpMethods()).iterator().next();
        return createHttpResponse(METHOD_NOT_ALLOWED, String.format(METHOD_IS_NOT_ALLOWED, supportedMethod));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponse> internalServerErrorException(Exception exception) {
        LOGGER.error(exception.getMessage());
        return createHttpResponse(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MSG);
    }

    @ExceptionHandler(NoResultException.class)
    public ResponseEntity<HttpResponse> notFoundException(NoResultException exception) {
        LOGGER.error(exception.getMessage());
        return createHttpResponse(NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<HttpResponse> iOException(IOException exception) {
        LOGGER.error(exception.getMessage());
        return createHttpResponse(INTERNAL_SERVER_ERROR, ERROR_PROCESSING_FILE);
    }

    private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message){

        return new ResponseEntity<>(new HttpResponse(httpStatus.value(),
                                            httpStatus,
                                            httpStatus.getReasonPhrase(),
                                            message
                                            ),
                                            httpStatus);
    }

    @RequestMapping(ERROR_PATH)
    public ResponseEntity<HttpResponse> notFound404() {
        return createHttpResponse(NOT_FOUND, "URL não encontrado...");
    }

    public String getErrorPath() {
        return ERROR_PATH;
    }
}
