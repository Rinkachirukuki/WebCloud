package com.itrev.WebCloud.Controllers;

import com.itrev.WebCloud.Archiver.Archiver;
import com.itrev.WebCloud.Files.FileMemory.FileManager;
import com.itrev.WebCloud.Models.Item;
import com.itrev.WebCloud.Repo.ItemRepository;
import com.itrev.WebCloud.Validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class FileController {
    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("/")
    public String Files(@RequestParam(defaultValue = "", value="info") String filter,
                        @RequestParam(defaultValue = "0000-01-07", value="fromD") String fromD,
                        @RequestParam(defaultValue = "2100-01-01", value="toD") String toD,
                        @RequestParam(defaultValue = "", value="type") String type,
                        Model model) throws Exception{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        model.addAttribute("fileInfo", FileManager.GetItemsInfo(filter,format.parse(fromD),format.parse(toD),type));
        model.addAttribute("dataTypes",FileManager.GetItemsTypes());
    return "Files";
    }
    @GetMapping("/Upload")
    public String FileUploader(@RequestParam(defaultValue = "0", value="error") int err, Model model) throws Exception{
        model.addAttribute("errInfo",Validator.getDescription(err));
        return "FileUploader";
    }
    @PostMapping("/Upload")
    public String FileUploader(@RequestParam("file") MultipartFile file) throws Exception{
        if(!file.isEmpty()){
            String name=file.getOriginalFilename();
            long size=file.getSize();
            if (!Validator.ValidateSize(size)) return "redirect:/?error=1";
            if (!Validator.ValidateType(name)) return "redirect:/?error=2";
            Item a = new Item(name,file.getContentType(),size,file.getBytes());
            FileManager.AddFile(a);
        }
        return "redirect:/Upload";
    }
    @PostMapping("/")
    public ResponseEntity<Object> add_file(@RequestParam("nameList") List<String> nameList) throws Exception {
        HttpHeaders headers = new HttpHeaders();

        ResponseEntity<Object> resEntity = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + new Date() + ".zip")
                .contentType(MediaType.parseMediaType("application/x-zip-compressed"))
                .body(Archiver.makeArchive(nameList));
        return resEntity;
    }
    public void foo(@RequestParam("nameList[]") List<String> nameList) {
        for(String number : nameList) {
            System.out.println(number);
        }
    }

    @GetMapping("/{FileName}")
    public String FileInfo(@PathVariable(value = "FileName") String name, Model model) throws Exception {
        Item res = FileManager.ReadFile(name);
        model.addAttribute("File", res);
        return "FileInfo";
    }
    @RequestMapping(value = "/d/{FileName}", method = RequestMethod.GET)
    public ResponseEntity<Object> FileDownload(@PathVariable(value = "FileName") String name) throws Exception {
        Item res = FileManager.ReadFile(name);
        InputStream inStr = new ByteArrayInputStream(res.getFile());
        InputStreamResource inRes = new InputStreamResource(inStr);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-disposition",String.format("attachment", res.getTitle()));
        ResponseEntity<Object> resEntity = ResponseEntity.ok().headers(headers)
                .contentType(MediaType.parseMediaType(res.getType()))
                .contentLength(res.getSize())
                .body(inRes);
        return resEntity;
    }
    @PostMapping("/{filename}")
    public String rename_file(@PathVariable("filename") String oldName, @RequestParam("rename") String newName) throws Exception {
        if(newName != ""){
            Item a = FileManager.ReadFile(oldName);
            FileManager.RenameFile(oldName,newName);
        }
        return "redirect:/";
    }
    @RequestMapping(value = "/remove/{FileName}", method = RequestMethod.GET)
    public String FileDelete(@PathVariable(value = "FileName") String name) throws Exception {
        FileManager.RemoveFile(name);
        return "redirect:/";
    }
}
