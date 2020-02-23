package net.test.controller;

import java.security.Principal;
import net.test.service.UserService;
import net.test.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
 
@Controller
public class MainController {
	
	@Autowired
    private UserService userService;
	
    @Autowired
    @Lazy
    private AuthenticationManager authManager;
 
    @RequestMapping(value = { "/", "/welcome" }, method = RequestMethod.GET)
    public String welcomePage(Model model) {
        model.addAttribute("title", "Welcome");
        model.addAttribute("message", "This is welcome page!");
        return "welcomePage";
    }
 
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminPage(Model model, Principal principal) {
         
        User loginedUser = (User) ((Authentication) principal).getPrincipal();
 
        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);
         
        return "adminPage";
    }
 
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(Model model) {
        return "loginPage";
    }
    
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registration() {
        return "register";
    }
    
    @RequestMapping(value = "/newUser", method = RequestMethod.POST)
    public String newUser(@RequestParam String username, @RequestParam String password, @RequestParam String email, Model model){
        /*
    	boolean userExist = userService.isExist(username);
        if(userExist){
            String rePassword = password;
            String reEmail = email;
            String nameError = "This name is already registered, try something else.";
            model.addAttribute("nameError",nameError);
            model.addAttribute("rePassword",rePassword);
            model.addAttribute("reEmail",reEmail);
            return "register";
        }
        */
        try{
        	System.out.println("username="+username+", password="+password+", email="+email);
            boolean isSuccess = userService.createUser(username, password);
            if(isSuccess) {
            	//After successfully Creating user
            	UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
                Authentication authentication = authManager.authenticate(authRequest);
                if (authentication.isAuthenticated()) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println(String.format("Auto login successfully!", username));
                }
            }
        } catch (Exception e){
            System.out.println("newUser exception: "+e);
        }

        return "redirect:/";
    }
 
    @RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
    public String logoutSuccessfulPage(Model model) {
        model.addAttribute("title", "Logout");
        return "logoutSuccessfulPage";
    }
 
    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    public String userInfo(Model model, Principal principal) {
 
        // After user login successfully.
        String userName = principal.getName();
 
        System.out.println("User Name: " + userName);
 
        User loginedUser = (User) ((Authentication) principal).getPrincipal();
 
        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);
 
        return "userInfoPage";
    }
 
    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public String accessDenied(Model model, Principal principal) {
 
        if (principal != null) {
            User loginedUser = (User) ((Authentication) principal).getPrincipal();
 
            String userInfo = WebUtils.toString(loginedUser);
 
            model.addAttribute("userInfo", userInfo);
 
            String message = "Hi " + principal.getName() //
                    + "<br> You do not have permission to access this page!";
            model.addAttribute("message", message);
 
        }
 
        return "403Page";
    }
    
}
