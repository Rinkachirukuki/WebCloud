package com.itrev.WebCloud.controllers;

import com.itrev.WebCloud.archiver.Archiver;
import com.itrev.WebCloud.files.FileMemory;
import com.itrev.WebCloud.models.Item;
import com.itrev.WebCloud.repo.ItemRepository;
import com.itrev.WebCloud.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMessage;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.converter.HttpMessageConverter;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class FrontController {
    @Autowired
    private ItemRepository itemRepository;
    private static final SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd");

    //просмотр файлов с использованием фильтра
    @GetMapping("/")
    public String files(@RequestParam(defaultValue = "", value="info") String filter,
                        @RequestParam(defaultValue = "0000-01-07", value="fromD") String fromD,
                        @RequestParam(defaultValue = "2100-01-01", value="toD") String toD,
                        @RequestParam(defaultValue = "", value="type") String type,
                        Model model) throws ParseException {

        model.addAttribute("fileInfo", FileMemory.getItemsInfo(filter,format.parse(fromD),format.parse(toD),type));
        model.addAttribute("dataTypes", FileMemory.getItemsTypes());
        return "Files";


    }

    //просмотр страницы загрузки
    @GetMapping("/Upload")
    public String fileUploader(@RequestBody HttpMessage json, Model model) {
        H
        return "FileUploader";
    }






    //просмотр информации о файле
    @GetMapping("/{FileName}")
    public String fileInfo(@PathVariable(value = "FileName") String name, Model model)  {
        try {
            Item res = FileMemory.readFile(name);
            model.addAttribute("File", res);
        }
        catch (FileMemory.FileMemoryException ex){
            model.addAttribute("errInfo",ex.getLocalizedMessage());
        }
        finally {
            return "FileInfo";
        }
    }




    //переименование файла
    @PostMapping("/{filename}")
    public String renameFile(@PathVariable("filename") String old_name, @RequestParam("rename") String new_name, Model model)  {
        try{
            if(new_name != ""){
                FileMemory.renameFile(old_name,new_name);
                old_name=new_name;
            }
            else{
                model.addAttribute("errorInfo","Пустое имя файла!");
            }
        }
        catch (FileMemory.FileMemoryException ex){
            model.addAttribute("errorInfo",ex.getLocalizedMessage());
        }
        finally {
            return "redirect:/";
        }
    }

    //удаление
    @GetMapping("/remove/{FileName}")
    public String fileDelete(@PathVariable(value = "FileName") String name, Model model) {
        try {
            FileMemory.removeFile(name);
        }
        catch (FileMemory.FileMemoryException ex){
            model.addAttribute("errInfo",ex.getLocalizedMessage());
        }
        finally {
            return "redirect:/";
        }
    }
}
