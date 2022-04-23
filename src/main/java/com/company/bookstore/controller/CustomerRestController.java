package com.company.bookstore.controller;

import java.util.List;

import javax.servlet.ServletContext;

import com.company.bookstore.model.Book;
import com.company.bookstore.model.OrderModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.company.bookstore.dao.BookDAO;
import com.company.bookstore.file.StorageService;

@RestController
@RequestMapping(path="/customer-rest")
public class CustomerRestController {

	@Autowired
	private StorageService storageService;
	
	@Autowired
	private ServletContext servletContext;
	
	@Autowired
	private BookDAO bookDAO;
	
	@GetMapping(path="/find-all",produces = MediaType.APPLICATION_XML_VALUE)
	public List<Book> showBooks() {
		 return addImagePath(bookDAO.findAll());		 
	}
	
	@GetMapping(path="/search/{search}",produces = MediaType.APPLICATION_XML_VALUE)
	public List<Book> findProductsSearch(@PathVariable(name="search") String search){
		return addImagePath( this.bookDAO.findAllSearch(search));
	}
	
	private List<Book> addImagePath(List<Book> books) {
		String contextPath = servletContext.getContextPath();
		System.out.println("contextPath : " + contextPath);
		for (Book book : books) {
			book.setImagePath(contextPath + "/files/" + book.getImagePath());
		}
		return books;
	}
	
	@PostMapping
	public void confirm(@RequestBody OrderModel orderModel){
		
	}
	
	
}
