package com.itrev.WebCloud.errors;

public class Error {
    private String errInfo;


    public String getErrInfo() {
        return errInfo;
    }

    public void setErrInfo(String errInfo) {
        this.errInfo = errInfo;
    }


    public Error(String errInfo) {
        this.errInfo = errInfo;
    }
}
