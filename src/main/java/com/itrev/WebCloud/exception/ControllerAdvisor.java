package com.itrev.WebCloud.exception;

import com.itrev.WebCloud.exception.FileMemoryExistingFileException;

import com.itrev.WebCloud.messages.Message;
import org.springframework.http.HttpStatus;
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
    public Message handleRuntimeException(RuntimeException ex){
        return new Message(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(FileMemoryExistingFileException.class)
    public Message handleFileMemoryException(FileMemoryExistingFileException ex){
        return new Message(ex.getLocalizedMessage(), HttpStatus.CONFLICT);
    }
    @ExceptionHandler(FileMemoryFileNotFoundException.class)
    public Message handleFileMemoryFileNotFoundException(FileMemoryFileNotFoundException ex){
        return new Message(ex.getLocalizedMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(IOException.class)
    public Message handleIOException(IOException ex){
        return new Message(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(ParseException.class)
    public Message ParseException(ParseException ex){
        return new Message(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(SizeLimitExceededException.class)
    public Message SizeLimitExceededException(SizeLimitExceededException ex){
        return new Message(ex.getLocalizedMessage(), HttpStatus.REQUEST_ENTITY_TOO_LARGE);
    }
    @ExceptionHandler(Exception.class)
    public Message handleException(Exception ex){
        return new Message(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
