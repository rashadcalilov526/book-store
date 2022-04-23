package com.company.bookstore.controller;

import com.company.bookstore.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.company.bookstore.dao.UserDAOImpl;
import com.company.bookstore.model.User;

@Controller
public class UserController {
	
   @Autowired
   private UserDAOImpl userDAOImpl;
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/index")
	public String index(){
		return "index";
	}
	@GetMapping("/login")
	public String showLoginPage(){
		Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
		if(authentication==null || authentication instanceof AnonymousAuthenticationToken){
			return "/login";
		}
		return "redirect:/";
	}
   @GetMapping(path="/createUserShowForm")
   public String showNewUserFormPage(Model model){
	   User user = new User();
	   model.addAttribute("user",user);
	   return "create-user-form";
   }
   
   @RequestMapping(value = "/createUserProcess", method = RequestMethod.POST)
	public String createUserProcess(  @ModelAttribute("user") User user,Model model) {
     String viewName="";
	  
	  boolean result=  userDAOImpl.createUser(user);
	if(result){
		model.addAttribute("userCreatedSuccessfully", "");
		System.out.println(user);
		viewName="login";
	}else{
		model.addAttribute("userAlreadyExists", "");
		viewName="create-user-form";
	}
		 
		return viewName;
	}
	
}
