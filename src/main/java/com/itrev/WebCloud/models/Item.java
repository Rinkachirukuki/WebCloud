package com.itrev.WebCloud.models;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id; //id для базы данных
    private String title; //имя файла
    private String type;// тип файла
    private long size; //размер файла
    private Date uploadDate;//дата загрузки
    private Date changeDate;//дата изменения
    private byte[] file; //содержимое файла

    public Item(){};
    public Item(String title, String type, long size, byte[] file) {
        this.title = title;
        this.type = type;
        this.size = size;
        this.file = file;
        this.uploadDate=new Date();
        this.changeDate=new Date();
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
        return  title + ' ' + type + ' ' + size + ' ' + uploadDate + ' ' + changeDate;
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
