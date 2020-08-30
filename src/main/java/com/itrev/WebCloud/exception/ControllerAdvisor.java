package com.itrev.WebCloud.exception;

import com.itrev.WebCloud.exception.FileMemoryException;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import javax.naming.SizeLimitExceededException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;

@RestControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public RuntimeException handleRuntimeException(RuntimeException ex){
        return ex;
    }
    @ExceptionHandler(FileMemoryException.class)
    public ResponseEntity<Object> handleFileMemoryException(FileMemoryException ex){
        return ResponseEntity.badRequest().body(ex);
    }
    @ExceptionHandler(IOException.class)
    public ResponseEntity<Object> handleIOException(IOException ex){
        return ResponseEntity.badRequest().body(ex);
    }
    @ExceptionHandler(ParseException.class)
    public ResponseEntity<Object> ParseException(ParseException ex){
        return ResponseEntity.badRequest().body(ex);
    }
    @ExceptionHandler(SizeLimitExceededException.class)
    public ResponseEntity<Object> SizeLimitExceededException(SizeLimitExceededException ex){
        return ResponseEntity.badRequest().body(ex);
    }


}
