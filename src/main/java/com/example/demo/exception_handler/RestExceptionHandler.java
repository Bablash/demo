package com.example.demo.exception_handler;

import com.example.demo.error_message.ControllerError;
import com.example.demo.exception.ForbiddenException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.NoSuchElementException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String error = String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
                        ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

        ControllerError controllerError = new ControllerError(ex.getMessage(), error);
        return new ResponseEntity<>(controllerError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NoSuchElementException.class, EntityNotFoundException.class})
    public ResponseEntity<Object> handleEntityNotFoundException(NoSuchElementException ex, WebRequest request) {
        ControllerError controllerError = new ControllerError("Entity Not Found Exception", ex.getMessage());
        return new ResponseEntity<>(controllerError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ForbiddenException.class})
    protected ResponseEntity<Object> handleForbiddenEditException(RuntimeException ex, WebRequest request) {
        ControllerError controllerError = new ControllerError("Forbidden edit", ex.getMessage());
        return new ResponseEntity<>(controllerError, HttpStatus.FORBIDDEN);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        ControllerError controllerError = new ControllerError("Malformed JSON Request", ex.getMessage());
        return new ResponseEntity<>(controllerError, status);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
                                                                   HttpStatusCode status, WebRequest request) {
        return new ResponseEntity<>(new ControllerError("No Handler Found", ex.getMessage()), status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .toList();
        ControllerError controllerError = new ControllerError("Method Argument Not Valid", ex.getMessage(), errors);
        return new ResponseEntity<>(controllerError, status);
    }
}
