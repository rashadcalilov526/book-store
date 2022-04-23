package com.company.bookstore.controller;

import com.company.bookstore.config.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.company.bookstore.dao.BookDAO;

@Controller
public class BasketController {
	
	@Autowired
	private BookDAO bookDAO;
	private String username;
	
	@Autowired
	private MyUserDetails myUserDetails;
	
	@GetMapping("/basket")
	public String show(){
		return "basket";
	}
	
}
