package fatec.grupodois.endurance.entity;

<<<<<<< HEAD
import lombok.AllArgsConstructor;
=======
import com.fasterxml.jackson.annotation.JsonFormat;
>>>>>>> 2bea5a457ac43bd4613ca51b12f002630fb5629f
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

<<<<<<< HEAD
@AllArgsConstructor
=======
import java.util.Date;

>>>>>>> 2bea5a457ac43bd4613ca51b12f002630fb5629f
@NoArgsConstructor
@Data
public class HttpResponse {

<<<<<<< HEAD
=======
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="MM-dd-yyyy hh:mm:ss", timezone = "GMT-3")
    private Date timeStamp;

>>>>>>> 2bea5a457ac43bd4613ca51b12f002630fb5629f
    private int httpStatusCode;

    private HttpStatus httpStatus;

    private String reasonPhrase;

    private String message;
<<<<<<< HEAD
=======

    public HttpResponse(int httpStatusCode, HttpStatus httpStatus, String reasonPhrase, String message) {
        this.timeStamp = new Date();
        this.httpStatusCode = httpStatusCode;
        this.httpStatus = httpStatus;
        this.reasonPhrase = reasonPhrase;
        this.message = message;
    }
>>>>>>> 2bea5a457ac43bd4613ca51b12f002630fb5629f
}
