package com.itrev.WebCloud.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class MainController {

	@GetMapping("/")
	public String greeting(Model model) {
		model.addAttribute("title", "Главная страница");
		return "home";
	}
        @PostMapping("/")
        public String add_file(@RequestParam("file") MultipartFile file){
            
            return "home";
        }

}