package com.itrev.WebCloud.controllers;

import com.itrev.WebCloud.archiver.Archiver;
import com.itrev.WebCloud.files.FileMemory;
import com.itrev.WebCloud.models.Item;
import com.itrev.WebCloud.repo.ItemRepository;
import com.itrev.WebCloud.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class FileController {
    @Autowired
    private ItemRepository itemRepository;
    private static final SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd");

    //просмотр файлов с использованием фильтра
    @GetMapping("/")
    public String files(@RequestParam(defaultValue = "", value="info") String filter,
                        @RequestParam(defaultValue = "0000-01-07", value="fromD") String fromD,
                        @RequestParam(defaultValue = "2100-01-01", value="toD") String toD,
                        @RequestParam(defaultValue = "", value="type") String type,
                        Model model) {
        try {
        model.addAttribute("fileInfo", FileMemory.getItemsInfo(filter,format.parse(fromD),format.parse(toD),type));
        model.addAttribute("dataTypes", FileMemory.getItemsTypes());
        }
        catch (ParseException ex){
            model.addAttribute("errInfo",ex.getLocalizedMessage());
        }
        finally {
            return "Files";
        }

    }

    //просмотр страницы загрузки
    @GetMapping("/Upload")
    public String fileUploader(@RequestParam(defaultValue = "", value="error") String err, Model model) {
        model.addAttribute("errInfo",err);
        return "FileUploader";
    }

    //загрузка файлов
    @PostMapping("/Upload")
    public String fileUploader(@RequestParam("file") MultipartFile file, Model model) {
        try {
        if(!file.isEmpty()){
            long size=file.getSize();
            if (!Validator.validateSize(size)) {
                model.addAttribute("errInfo","Ошибка валидации. Превышен максимальный размер файла (15 Мбайт)");
                return "FileUploader";
            }
            String name=file.getOriginalFilename();
            if (!Validator.validateType(name)){
                model.addAttribute("errInfo","Ошибка валидации. Невозможно определить формат файла");
                return "FileUploader";
            }
            Item a = new Item(name,file.getContentType(),size,file.getBytes());
            FileMemory.addFile(a);
            }
        }
        catch (IOException ex){
            model.addAttribute("errInfo","Ошибка загрузки файла!");
        }
        finally {
            return "FileUploader";
        }
    }

    //скачивание архива
    @PostMapping("/")
    public ResponseEntity<Object> downloadArchive(@RequestParam(defaultValue = "", value="nameList") List<String> nameList)  {
        try {
            if (nameList.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + new Date() + ".zip")
                .contentType(MediaType.parseMediaType("application/x-zip-compressed"))
                .body(Archiver.getArchive(nameList));
        }
        catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getLocalizedMessage());
        }
    }

    //просмотр информации о файле
    @GetMapping("/{FileName}")
    public String fileInfo(@PathVariable(value = "FileName") String name, Model model)  {
        try {
            Item res = FileMemory.readFile(name);
            model.addAttribute("File", res);
        }
        catch (FileMemory.MemoryException ex){
            model.addAttribute("errInfo",ex.getLocalizedMessage());
        }
        finally {
            return "FileInfo";
        }
    }

    //скачивание файла
    @GetMapping("/d/{FileName}")
    public ResponseEntity<Object> fileDownload(@PathVariable(value = "FileName") String name, Model model) {
        try {
            Item res = FileMemory.readFile(name);
            InputStream inStr = new ByteArrayInputStream(res.getFile());
            InputStreamResource inRes = new InputStreamResource(inStr);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-disposition",String.format("attachment", res.getTitle()));
            return ResponseEntity.ok().headers(headers)
                    .contentType(MediaType.parseMediaType(res.getType()))
                    .contentLength(res.getSize())
                    .body(inRes);
        }
        catch (FileMemory.MemoryException ex) {
            model.addAttribute("errInfo", ex.getLocalizedMessage());
            return ResponseEntity.noContent().build();
        }
    }

    //переименование файла
    @PostMapping("/{filename}")
    public String renameFile(@PathVariable("filename") String old_name, @RequestParam("rename") String new_name, Model model)  {
        try{
            if(new_name != ""){
                FileMemory.renameFile(old_name,new_name);
            }
        }
        catch (FileMemory.MemoryException ex){
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
        catch (FileMemory.MemoryException ex){
            model.addAttribute("errInfo",ex.getLocalizedMessage());
        }
        finally {
            return "redirect:/";
        }
    }
}
