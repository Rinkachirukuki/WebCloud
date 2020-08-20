package com.itrev.WebCloud.Controllers;

import com.itrev.WebCloud.Models.Item;
import com.itrev.WebCloud.Repo.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Controller
public class FileController {
    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("/Files")
    public String Files(Model model){
        Iterable<Item> items = itemRepository.findAll();
        model.addAttribute("items",items);
    return "Files";
    }
    @GetMapping("/{id}")
    public String FileInfo(@PathVariable(value = "id") Long id, Model model){
        Optional<Item> item = itemRepository.findById(id);
        ArrayList<Item> res = new ArrayList<>();
        item.ifPresent(res::add);
        model.addAttribute("File", res);
        return "FileInfo";
    }
}
