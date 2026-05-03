package uz.pdp.healthsphere.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InvalidTimeRangeException extends RuntimeException {

    private final HttpStatus status;

    public InvalidTimeRangeException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}
