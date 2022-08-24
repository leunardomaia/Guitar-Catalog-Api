package br.com.leunardomaia.Guitar.Catalog.config.handler;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalHandler {

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<Object> propertyReferenceException(PropertyReferenceException e){
        return new ResponseEntity<>(new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), "Sort field doesn't exist"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> httpMessageNotReadableException(){
        return new ResponseEntity<>(new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), "Required request body is missing or it might have some error."),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> methodArgumentNotValidException(MethodArgumentNotValidException e){
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> messages = new ArrayList<>();
        fieldErrors.forEach( error -> messages.add(error.getField() + " " + error.getDefaultMessage()));
        return new ResponseEntity<>(new ExceptionResponses(HttpStatus.BAD_REQUEST.value(), messages), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e){
        return new ResponseEntity<>(new ExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                "'" + e.getName() + "' should be a valid " + e.getRequiredType().getSimpleName() +" and '" + e.getValue() + "' isn't"),
                HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<Object> emptyResultDataAccessException(){
        return new ResponseEntity<>(new ExceptionResponse(HttpStatus.NOT_FOUND.value(), "Guitar doesn't exist."), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> entityNotFoundException(){
        return new ResponseEntity<>(new ExceptionResponse(HttpStatus.NOT_FOUND.value(), "Guitar doesn't exist."), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e){
        return new ResponseEntity<>(new ExceptionResponse(HttpStatus.METHOD_NOT_ALLOWED.value(), e.getMessage()), HttpStatus.METHOD_NOT_ALLOWED);
    }


}
