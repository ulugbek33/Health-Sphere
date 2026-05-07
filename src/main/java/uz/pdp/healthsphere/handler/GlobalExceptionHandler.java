package uz.pdp.healthsphere.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uz.pdp.healthsphere.dto.error.ErrorDTO;
import uz.pdp.healthsphere.dto.error.FieldErrorDTO;
import uz.pdp.healthsphere.exceptions.*;

import java.util.List;

@Service
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handler(MethodArgumentNotValidException exception) {

        List<FieldErrorDTO> fieldErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new FieldErrorDTO(fieldError.getField(), fieldError.getDefaultMessage())).toList();

        ErrorDTO errorDTO = new ErrorDTO(
                "Validation failed",
                400
        );

        errorDTO.setFieldErrors(fieldErrors);

        return ResponseEntity.status(400).body(errorDTO);
    }

    @ExceptionHandler(EntityAlreadyException.class)
    public ResponseEntity<ErrorDTO> handler(EntityAlreadyException exception) {

        ErrorDTO errorDTO = new ErrorDTO(
                exception.getMessage(),
                exception.getStatus().value()
        );

        return ResponseEntity.status(exception.getStatus()).body(errorDTO);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDTO> handler(EntityNotFoundException exception) {

        ErrorDTO errorDTO = new ErrorDTO(
                exception.getMessage(),
                exception.getStatus().value()
        );

        return ResponseEntity.status(exception.getStatus()).body(errorDTO);
    }

    @ExceptionHandler(PasswordIncorrectException.class)
    public ResponseEntity<ErrorDTO> handler(PasswordIncorrectException exception) {

        ErrorDTO errorDTO = new ErrorDTO(
                exception.getMessage(),
                exception.getStatus().value()
        );

        return ResponseEntity.status(exception.getStatus()).body(errorDTO);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDTO> handler(AccessDeniedException exception) {

        ErrorDTO errorDTO = new ErrorDTO(
                "Forbidden : " + exception.getMessage(),
                403
        );

        return ResponseEntity.status(403).body(errorDTO);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDTO> handler(RuntimeException exception) {

        ErrorDTO errorDTO = new ErrorDTO(
                "Internal server error :" + exception.getMessage(),
                500
        );

        return ResponseEntity.status(500).body(errorDTO);
    }

    @ExceptionHandler(AppointmentConflictException.class)
    public ResponseEntity<ErrorDTO> handler(AppointmentConflictException exception) {

        ErrorDTO errorDTO = new ErrorDTO(
                exception.getMessage(),
                exception.getStatus().value()
        );

        return ResponseEntity.status(exception.getStatus()).body(errorDTO);
    }

    @ExceptionHandler(InvalidTimeRangeException.class)
    public ResponseEntity<ErrorDTO> handler(InvalidTimeRangeException exception) {

        ErrorDTO errorDTO = new ErrorDTO(
                exception.getMessage(),
                exception.getStatus().value()
        );

        return ResponseEntity.status(exception.getStatus()).body(errorDTO);
    }

    @ExceptionHandler(AppointmentStatusException.class)
    public ResponseEntity<ErrorDTO> handler(AppointmentStatusException exception) {

        ErrorDTO errorDTO = new ErrorDTO(
                exception.getMessage(),
                exception.getStatus().value()
        );

        return ResponseEntity.status(exception.getStatus()).body(errorDTO);
    }

    @ExceptionHandler(OutOfStockException.class)
    public ResponseEntity<ErrorDTO> handler(OutOfStockException exception) {

        ErrorDTO errorDTO = new ErrorDTO(
                exception.getMessage(),
                exception.getStatus().value()
        );

        return ResponseEntity.status(exception.getStatus()).body(errorDTO);
    }


}
