package uz.pdp.healthsphere.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AppointmentConflictException extends RuntimeException {

    private final HttpStatus status;

    public AppointmentConflictException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}
