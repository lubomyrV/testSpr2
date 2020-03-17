package net.test.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.test.entity.AppUser;
import net.test.entity.Response;
import net.test.entity.UserWrapper;
import net.test.service.RoleService;
import net.test.service.UserService;

@RestController
public class MainRestController {
	
	@Autowired
    private UserService userService;
	
	@Autowired
    private RoleService roleService;
	
	@GetMapping("/getRoles")
	public Response getRoles() {
		List<String> resultList = roleService.getAllRoleNames();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = "";
		try {
			jsonString = mapper.writeValueAsString(resultList);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	    return new Response("Done", jsonString);
	}
	
	@PostMapping("/getUserInfo")
	public Response getUserInfo(@RequestBody String userId) {
		//System.out.println("getUserInfo="+userId);
		List<Object> resultList = userService.getUserWithRoles(userId);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = "";
		try {
			jsonString = mapper.writeValueAsString(resultList);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	    return new Response("Done", jsonString);
	}
	
	@PostMapping("/search")
	public Response searchUser(@RequestBody String usernameOrEmail) {
		//System.out.println("usernameOrEmail="+usernameOrEmail);
	    List<AppUser> users = userService.findUsersByUserameOrEmail(usernameOrEmail);
	    // Java object to JSON string
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = "";
		try {
			jsonString = mapper.writeValueAsString(users);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	    return new Response("Done", jsonString);
	}
	
	@PostMapping(value = "/updateUser")
	public Response postCustomer(@RequestBody String jUser) {	    
		ObjectMapper objectMapper = new ObjectMapper();
		System.out.println("jUser "+jUser);
		try {
			// JSON string to Java object
			UserWrapper userWrap = objectMapper.readValue(jUser, UserWrapper.class);
			System.out.println("userWrap "+userWrap);
			userService.updateUser(userWrap);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	    return new Response("Done", "ok");
	}
	
}
