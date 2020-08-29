package com.itrev.WebCloud.exception;

import com.itrev.WebCloud.files.FileMemory;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {
    @ExceptionHandler(FileMemory.FileMemoryException.class)
    public ResponseEntity<Object> handleFileMemoryException(FileMemory.FileMemoryException ex, WebRequest request, Model model){
        model.addAttribute("errorInfo",ex.getLocalizedMessage());
        return ResponseEntity.noContent().build();
    }
    @ExceptionHandler(IOException.class)
    public ResponseEntity<Object> handleIOException(FileMemory.FileMemoryException ex, WebRequest request, Model model){
        model.addAttribute("errorInfo",ex.getLocalizedMessage());
        return ResponseEntity.noContent().build();
    }
    
}
