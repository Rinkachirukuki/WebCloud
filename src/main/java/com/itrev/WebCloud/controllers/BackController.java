package com.itrev.WebCloud.controllers;

import com.itrev.WebCloud.archiver.Archiver;
import com.itrev.WebCloud.exception.FileMemoryException;
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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class BackController {
    //полный список моделей данных
    private static final SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd");
    @GetMapping("/json")
    public List<Item> files() {
        return FileMemory.getItemsInfoForJson();
    }

    @GetMapping("/json/{fileName}")
    public Item fileInfo(@PathVariable(value = "FileName") String name, Model model) throws FileMemoryException {
       return FileMemory.readFile(name);
    }

    @GetMapping("/json/fileList")
    public String[] fileList() {
        return FileMemory.getFileNames();
    }
    //фильтрованный список моделей данных
    @GetMapping("/json/filter")
    public List<Item> files(@RequestParam(defaultValue = "", value="info") String filter,
                            @RequestParam(defaultValue = "0000-01-07", value="fromD") String fromD,
                            @RequestParam(defaultValue = "2100-01-01", value="toD") String toD,
                            @RequestParam(defaultValue = "", value="type") String type) throws ParseException {
        return FileMemory.getItemsInfoForJson(filter,format.parse(fromD),format.parse(toD),type);
    }
    //скачивание архива
    @GetMapping("/da")
    public ResponseEntity<Object> downloadArchive(@RequestParam(defaultValue = "", value="nameList") List<String> nameList) throws Exception
{           if (nameList.isEmpty()) return ResponseEntity.noContent().build();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + new Date() + ".zip")
                    .contentType(MediaType.parseMediaType("application/x-zip-compressed"))
                    .body(Archiver.getArchive(nameList));
    }
    //скачивание файла
    @GetMapping("/d/{FileName}")
    public ResponseEntity<Object> fileDownload(@PathVariable(value = "FileName") String name) throws Exception {
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
    //загрузка файлов
    @RequestMapping(method = RequestMethod.POST, value = "/Upload",headers="Accept=application/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> fileUploader(@RequestParam("file") MultipartFile file) throws IOException,FileMemoryException{

        if(!file.isEmpty()){
            long size=file.getSize();
            if (!Validator.validateSize(size)) {
                return ResponseEntity.badRequest().headers(new HttpHeaders())
                        .contentType(MediaType.parseMediaType("application/json"))
                        .body(new Error("Ошибка валидации! Невозможно определить формат файла"));
            }
            String name=file.getOriginalFilename();
            if (!Validator.validateType(name)){
                return ResponseEntity.badRequest().headers(new HttpHeaders())
                        .contentType(MediaType.parseMediaType("application/json"))
                        .body(new Error("Ошибка валидации! Невозможно определить формат файла"));
            }
            Item a = new Item(name,file.getContentType(),size,file.getBytes());
            FileMemory.addFile(a);
        }
        return ResponseEntity.ok().headers(new HttpHeaders())
                .contentType(MediaType.parseMediaType("application/json")).body("Файл успешно загружен");
    }
    @RequestMapping(method = RequestMethod.POST, value = "/{filename}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> renameFile(@PathVariable("filename") String old_name, @RequestParam("rename") String new_name)  throws FileMemoryException {
            if(new_name != ""){
                FileMemory.renameFile(old_name,new_name);
                return ResponseEntity.ok().headers(new HttpHeaders())
                        .contentType(MediaType.parseMediaType("application/json")).body("Файл успешно переименован!");
            }
            else return ResponseEntity.badRequest().headers(new HttpHeaders())
                .contentType(MediaType.parseMediaType("application/json"))
                .body(new Error("Ошибка! Имя файла не может быть пустым"));
    }

    //удаление
    @RequestMapping(method = RequestMethod.GET, value = "/remove/{FileName}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> fileDelete(@PathVariable(value = "FileName") String name) throws FileMemoryException {
        FileMemory.removeFile(name);
        return ResponseEntity.ok().headers(new HttpHeaders())
                .contentType(MediaType.parseMediaType("application/json")).body("Файл успешно удалён!");
    }
}
