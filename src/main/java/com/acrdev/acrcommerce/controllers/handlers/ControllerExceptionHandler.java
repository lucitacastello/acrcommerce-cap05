package com.acrdev.acrcommerce.controllers.handlers;

import com.acrdev.acrcommerce.dto.CustomErrorDTO;
import com.acrdev.acrcommerce.dto.ValidationErrorDTO;
import com.acrdev.acrcommerce.services.exceptions.DatabaseException;
import com.acrdev.acrcommerce.services.exceptions.ForbiddenException;
import com.acrdev.acrcommerce.services.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomErrorDTO> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request){
        HttpStatus status = HttpStatus.NOT_FOUND;
        CustomErrorDTO err = new CustomErrorDTO(Instant.now(), status.value(),  e.getMessage(), request.getRequestURI() );
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<CustomErrorDTO> database(DatabaseException e, HttpServletRequest request){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        CustomErrorDTO err = new CustomErrorDTO(Instant.now(), status.value(),  e.getMessage(), request.getRequestURI() );
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorDTO> methodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request){
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ValidationErrorDTO err = new ValidationErrorDTO(Instant.now(), status.value(),  "Dados Inválidos!", request.getRequestURI() );

        for(FieldError f : e.getBindingResult().getFieldErrors()){
            err.addError(f.getField(), f.getDefaultMessage());
        }

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<CustomErrorDTO> forbidden(ForbiddenException e, HttpServletRequest request){
        HttpStatus status = HttpStatus.FORBIDDEN; //403
        CustomErrorDTO err = new CustomErrorDTO(Instant.now(), status.value(),  e.getMessage(), request.getRequestURI() );
        return ResponseEntity.status(status).body(err);
    }

}
