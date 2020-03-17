package com.example.demo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.AppUser;
import com.example.demo.service.UserService;
import com.example.demo.utils.WebUtils;

@Controller
public class AdminController {
	
	@Autowired
    private UserService userService;
	
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminPage(Model model, Principal principal) {
        User loginedUser = (User) ((Authentication) principal).getPrincipal();
        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);        
        return "adminPage";
    }
    
    @RequestMapping(value = "/shearchUsers", method = RequestMethod.GET)
    public String shearchUsers(Model model, @RequestParam String nameOrEmail) {
        List<AppUser> users = userService.findUsersByUserameOrEmail(nameOrEmail);
        model.addAttribute("users",users);
        return "adminPage";
    }
	
	@GetMapping("/admin/editUser-{id}")
    public String page(@PathVariable("id") int id, Model model) {
		System.out.println("id="+id);
		model.addAttribute("id", id);
        return "editUserPage";
    }
	
}
