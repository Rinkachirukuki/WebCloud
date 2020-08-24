package com.itrev.WebCloud.Controllers;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Controller
public class FileController {
    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("/")
    public String Files(@RequestParam(defaultValue = "0", value="error") int err,@RequestParam(defaultValue = "0", value="error") String filter, Model model){
        model.addAttribute("fileInfo", FileManager.GetItemsInfo());
        model.addAttribute("errInfo",Validator.getDescription(err));
    return "Files";
    }

    @PostMapping("/")
    public String add_file(@RequestParam("file") MultipartFile file) throws IOException {
        if(!file.isEmpty()){
            String name=file.getOriginalFilename();
            long size=file.getSize();
            if (!Validator.ValidateSize(size)) return "redirect:/?error=1";
            if (!Validator.ValidateType(name)) return "redirect:/?error=2";
            Item a = new Item(name,file.getContentType(),size,file.getBytes());
            FileManager.AddFile(a);
        }
        return "redirect:/";
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
