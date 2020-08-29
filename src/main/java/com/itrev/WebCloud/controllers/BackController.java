package com.itrev.WebCloud.controllers;

import com.itrev.WebCloud.archiver.Archiver;
import com.itrev.WebCloud.files.FileMemory;
import com.itrev.WebCloud.models.Item;
import com.itrev.WebCloud.validator.Validator;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

@RestController
public class BackController {

    //скачивание архива
    @GetMapping("/da")
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
    //скачивание файла
    @GetMapping("/d/{FileName}")
    public ResponseEntity<Object> fileDownload(@PathVariable(value = "FileName") String name, Model model) throws Exception {
        try {
            Item res = FileMemory.readFile(name);
            InputStream inStr = new ByteArrayInputStream(res.getFile());
            InputStreamResource inRes = new InputStreamResource(inStr);
            inStr.close();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-disposition",String.format("attachment", res.getTitle()));
            return ResponseEntity.ok().headers(headers)
                    .contentType(MediaType.parseMediaType(res.getType()))
                    .contentLength(res.getSize())
                    .body(inRes);
        }

        catch (IOException ex){
            model.addAttribute("errInfo", ex.getLocalizedMessage());
            return ResponseEntity.noContent().build();
        }
    }
    //загрузка файлов
    @PostMapping("/Upload")
    public ResponseEntity fileUploader(@RequestParam("file") MultipartFile file) throws IOException{
        if(!file.isEmpty()){
            long size=file.getSize();
            if (!Validator.validateSize(size)) {
                return ResponseEntity.ok().headers(new HttpHeaders())
                        .contentType(MediaType.parseMediaType("application/json"))
                        .body("{" +
                                "\"errInfo\": \"Ошибка валидации! Файл превышает максимальный размер (15 Мбайт)!\"" +
                                "}");
            }
            String name=file.getOriginalFilename();
            if (!Validator.validateType(name)){
                return ResponseEntity.ok().headers(new HttpHeaders())
                        .contentType(MediaType.parseMediaType("application/json"))
                        .body("{" +
                                "\"errInfo\": \"Ошибка валидации! Невозможно определить формат файла! \"" +
                                "}");
            }
            Item a = new Item(name,file.getContentType(),size,file.getBytes());
            FileMemory.addFile(a);
        }
        return ResponseEntity.noContent().build();
    }
}
