package uz.pdp.healthsphere.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class OutOfStockException extends RuntimeException {

    private final HttpStatus status;

    public OutOfStockException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}
