package fatec.grupodois.endurance.error;

public class EventoNotFoundException extends Exception{

    public EventoNotFoundException(String message){
        super(message);
    }

    public EventoNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventoNotFoundException(Throwable cause) {
        super(cause);
    }

    protected EventoNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
