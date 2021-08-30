/*package fatec.grupodois.endurance.entity;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class StatusEventoConverter implements AttributeConverter<StatusEvento, Integer> {


    @Override
    public Integer convertToDatabaseColumn(StatusEvento status) {
        if(status == null) {
            return null;
        }

        return status.getCode();
    }

    @Override
    public StatusEvento convertToEntityAttribute(Integer code) {
        if(code == null) {
            return null;
        }

        return Stream.of(StatusEvento.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}*/
