package com.api.assemblyvoter.exceptions;

import com.api.assemblyvoter.dto.response.ResponseHandler;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class ValidateParametersController {

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e) {
        return ResponseHandler.generateResponse(formatterMessage(e), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseHandler.generateResponse(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage(), HttpStatus.BAD_REQUEST);
    }

    private String formatterMessage(Exception e) {
        return e.getMessage().substring(e.getMessage().indexOf(":"));
    }
}
