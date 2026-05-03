package uz.pdp.healthsphere.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AppointmentStatusException extends RuntimeException {

    private final HttpStatus status;

    public AppointmentStatusException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}
