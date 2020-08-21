package com.itrev.WebCloud.Controllers;

import com.itrev.WebCloud.Files.FileMemory.FileManager;
import com.itrev.WebCloud.Models.Item;
import com.itrev.WebCloud.Repo.ItemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Controller
public class FileController {
    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("/Files")
    public String Files(Model model){
        String[] names = FileManager.GetFileNames();
        model.addAttribute("items",names);
    return "Files";
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
}
