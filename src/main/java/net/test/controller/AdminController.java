package net.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AdminController {
	
	@GetMapping("/admin/editUser-{id}")
    public String page(@PathVariable("id") int id, Model model) {
		System.out.println("id="+id);
		model.addAttribute("id", id);
        return "editUserPage";
    }
	
}
