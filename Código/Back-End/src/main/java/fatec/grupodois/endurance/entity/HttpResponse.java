package fatec.grupodois.endurance.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HttpResponse {

    private int httpStatusCode;

    private HttpStatus httpStatus;

    private String reasonPhrase;

    private String message;
}
