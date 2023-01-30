package org.testproject.drones.exception.handler;

import lombok.Getter;
import org.postgresql.util.PSQLException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.testproject.drones.exception.DroneIsNotAvailableException;
import org.testproject.drones.exception.DroneNotFoundException;
import org.testproject.drones.exception.NotEnoughSpaceException;

import java.time.Instant;
import java.util.stream.Collectors;

@ControllerAdvice
public class DroneResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                               HttpHeaders headers, HttpStatusCode status,
                                                               WebRequest request) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return handleExceptionInternal(ex, new ErrorResponse(message),
                defaultHttpHeaders(),
                HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(DroneNotFoundException.class)
    public ResponseEntity<Object> handleDroneNotFoundException(final DroneNotFoundException ex,
                                                               final WebRequest request) {
        return handleExceptionInternal(ex, new ErrorResponse(ex.getMessage()),
                defaultHttpHeaders(),
                HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(DroneIsNotAvailableException.class)
    public ResponseEntity<Object> handleDroneNotFoundException(final DroneIsNotAvailableException ex,
                                                               final WebRequest request) {
        return handleExceptionInternal(ex, new ErrorResponse(ex.getMessage()),
                defaultHttpHeaders(),
                HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(NotEnoughSpaceException.class)
    public ResponseEntity<Object> handleDroneNotFoundException(final NotEnoughSpaceException ex,
                                                               final WebRequest request) {
        return handleExceptionInternal(ex, new ErrorResponse(ex.getMessage()),
                defaultHttpHeaders(),
                HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(PSQLException.class)
    public ResponseEntity<Object> handleDroneNotFoundException(final PSQLException ex,
                                                               final WebRequest request) {
        return handleExceptionInternal(ex, new ErrorResponse(ex.getMessage()),
                defaultHttpHeaders(),
                HttpStatus.BAD_REQUEST, request);
    }

    private HttpHeaders defaultHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }

    @Getter
    private static class ErrorResponse {
        private final String message;
        private final Instant timestamp;

        public ErrorResponse(String message) {
            this.message = message;
            this.timestamp = Instant.now();
        }
    }
}
