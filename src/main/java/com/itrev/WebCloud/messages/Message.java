package com.itrev.WebCloud.messages;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message {

    private String info;

    private HttpStatus code;

    private List<String> validationErorrs;

    public Message() {
    }

    public Message(String info, HttpStatus code) {
        this.info = info;
        this.code = code;
    }

    public Message(String info, HttpStatus code, List<String> errors) {
        this.info = info;
        this.code = code;
        this.validationErorrs=errors;
    }

    public HttpStatus getCode() {
        return code;
    }

    public void setCode(HttpStatus code) {
        this.code = code;
    }

    public List<String> getValidationErorrs() {
        return validationErorrs;
    }

    public void setValidationErorrs(List<String> validationErorrs) {
        this.validationErorrs = validationErorrs;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
