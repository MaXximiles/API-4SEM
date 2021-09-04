package fatec.grupodois.endurance.error;

public class EventoInicioAfterException extends Exception{

    public EventoInicioAfterException(String message){
        super(message);
    }

    public EventoInicioAfterException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventoInicioAfterException(Throwable cause) {
        super(cause);
    }

    protected EventoInicioAfterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
