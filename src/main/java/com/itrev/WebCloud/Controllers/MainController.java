package com.itrev.WebCloud.Controllers;

import com.itrev.WebCloud.Models.Item;
import com.itrev.WebCloud.Repo.ItemRepository;
import com.itrev.WebCloud.Files.FileMemory.FileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Controller
public class MainController {
	@Autowired
	private ItemRepository itemRepository;
	@GetMapping("/")
	public String greeting(Model model) {
		model.addAttribute("title", "Главная страница");
		return "home";
	}
        @PostMapping("/")
        public String add_file(@RequestParam("file") MultipartFile file) throws IOException {
            if(file != null){
            	Item a = new Item(file.getOriginalFilename(),file.getContentType(),file.getSize(),file.getBytes());
                FileManager.AddFile(a);
        }
            return "home";
        }

}