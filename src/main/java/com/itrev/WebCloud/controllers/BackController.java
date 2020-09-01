package com.itrev.WebCloud.controllers;

import com.itrev.WebCloud.archiver.Archiver;
import com.itrev.WebCloud.exception.FileMemoryExistingFileException;
import com.itrev.WebCloud.exception.FileMemoryFileNotFoundException;

import com.itrev.WebCloud.files.FileMemory;
import com.itrev.WebCloud.messages.Message;
import com.itrev.WebCloud.models.Item;
import com.itrev.WebCloud.validator.Validator;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import java.util.ArrayList;
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
    public Item fileInfo(@PathVariable(value = "FileName") String name, Model model) throws FileMemoryFileNotFoundException {
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
    public ResponseEntity<Object> fileDownload(@PathVariable(value = "FileName") String name) throws FileMemoryFileNotFoundException,IOException {
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
    public Message fileUploader(@RequestParam("file") MultipartFile file) throws IOException,FileMemoryExistingFileException{
        Message resp=new Message("Файл пуст!",HttpStatus.BAD_REQUEST);
        List<String> errors=new ArrayList<>();
        if(!file.isEmpty()){
            long size=file.getSize();
            String name=file.getOriginalFilename();
            if (!Validator.validateSize(size)) {
                resp.setInfo("Ошибка валидации!");
                resp.setCode(HttpStatus.BAD_REQUEST);
                errors.add("Превышен максимальный размер файла!");

            }
            if (!Validator.validateType(name)){
                    resp.setInfo("Ошибка валидации!");
                    resp.setCode(HttpStatus.BAD_REQUEST);
                    errors.add("Невозможно определить формат файла!");
                }
            if(errors.isEmpty()){
                    Item a = new Item(name,file.getContentType(),size,file.getBytes());
                    FileMemory.addFile(a);
                    resp.setInfo("Файл успешно загружен!");
                    resp.setCode(HttpStatus.CREATED);

                }

            }

        if(!errors.isEmpty()) resp.setValidationErorrs(errors);
        return resp;
    }
    @RequestMapping(method = RequestMethod.POST, value = "/{filename}",produces = MediaType.APPLICATION_JSON_VALUE)
    public Message renameFile(@PathVariable("filename") String old_name, @RequestParam("rename") String new_name)  throws FileMemoryFileNotFoundException {
            if(new_name != ""){
                FileMemory.renameFile(old_name,new_name);
                return new Message("Файл успешно переименован!", HttpStatus.OK);
            }
            else return new Message("Имя файла не может быть пустым!", HttpStatus.BAD_REQUEST);
    }

    //удаление
    @RequestMapping(method = RequestMethod.GET, value = "/remove/{FileName}",produces = MediaType.APPLICATION_JSON_VALUE)
    public Message fileDelete(@PathVariable(value = "FileName") String name) throws FileMemoryFileNotFoundException {
        FileMemory.removeFile(name);
        return new Message("Файл успешно удалён!", HttpStatus.OK);
    }
}
