package com.itrev.WebCloud.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Date;

import javax.validation.constraints.*;
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private long id; //id для базы данных
    @NotNull
    @JsonProperty("title")
    private String title; //имя файла
    @JsonProperty("type")
    private String type;// тип файла
    @JsonProperty("size")
    private long size; //размер файла
    @JsonProperty("uploadDate")
    private Date uploadDate;//дата загрузки
    @JsonProperty("changeDate")
    private Date changeDate;//дата изменения
    @JsonIgnore
    private byte[] file; //содержимое файла
    @JsonProperty("link")
    private String link; //ссылка для файла

    public Item(){};
    public Item(String title, String type, long size, byte[] file) {
        this.title = title;
        this.type = type;
        this.size = size;
        this.file = file;
        this.uploadDate=new Date();
        this.changeDate=new Date();
        this.link="/d/"+title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date upload_date) {
        this.uploadDate = upload_date;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date change_date) {
        this.changeDate = change_date;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return  title + ' ' + type + ' ' + convSize() + ' ' + String.valueOf(uploadDate) + ' ' + String.valueOf(changeDate);
    }
    public String[] toStringArray() {

        return  new String[] {title, type, convSize(),  String.valueOf(uploadDate),  String.valueOf(changeDate)};
    }

    private String convSize(){
        double conv=size/1024;
        if(conv>=1){
            double convm= conv/1024;
            if(convm>=1) return String.valueOf(convm).substring(0,4)+" Мбайт";
            else return String.valueOf(conv)+" Кбайт";
        }
        else return String.valueOf(size)+" байт";
    }
}
