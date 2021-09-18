package fatec.grupodois.endurance.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Date;

@NoArgsConstructor
@Data
public class HttpResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="MM-dd-yyyy hh:mm:ss", timezone = "GMT-3")
    private Date timeStamp;

    private int httpStatusCode;

    private HttpStatus httpStatus;

    private String reasonPhrase;

    private String message;

    public HttpResponse(int httpStatusCode, HttpStatus httpStatus, String reasonPhrase, String message) {
        this.timeStamp = new Date();
        this.httpStatusCode = httpStatusCode;
        this.httpStatus = httpStatus;
        this.reasonPhrase = reasonPhrase;
        this.message = message;
    }
}
