package com.company.bookstore.controller;

import java.util.List;

import javax.servlet.ServletContext;

import com.company.bookstore.config.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.company.bookstore.dao.BookDAO;
import com.company.bookstore.dao.OrderDAO;
import com.company.bookstore.file.StorageService;
import com.company.bookstore.model.Book;


@Controller
public class BookController {

  @Autowired
  public BookDAO bookDAO;
  private String username;
  
  @Autowired
  public OrderDAO orderDAO;
  
  @Autowired
  private StorageService storageService;
  
  @Autowired
  private ServletContext servletContext;
  
  @Autowired
  private MyUserDetails myUserDetails;
	
  @GetMapping(path={"/","/home"})
  public String showHomePage(){
	  return "home";
  }
  
  @GetMapping(path="/showLoginPage")
  public String returnLoginPage(){
	  return "login";
  }
  
  
  @GetMapping(path="/books")
  public String showBookListPage(Model model){
	  Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
	  String username = loggedInUser.getName();
	  myUserDetails.setMessage("Hello session");
	  myUserDetails.setUsername(username);
	  this.username=username;
	  model.addAttribute("books", addImagePath(bookDAO.findAllByUsername(this.username)));
	  return "book-list";
  }
  
  @GetMapping(path="/orders")
  public String showOrdersPage(Model model){
	  Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
	  String username = loggedInUser.getName();
	  myUserDetails.setMessage("Hello session");
	  myUserDetails.setUsername(username);
	  this.username=username;
	  model.addAttribute("orders", orderDAO.findAllByUsername(this.username));
	  return "orders";
  }
  
  @GetMapping(path="/order-confirmation-message")
  public String showOrderConfirmationMessage(){
	  return "order-confirmation-form";
  }
  
  
  @GetMapping(path="/addbook")
  public String showAddBookForm(Model model){
	  Book book = new Book();
	  book.setId(0);
	  model.addAttribute("whenAddForm","");
	  model.addAttribute("book",book);
	  return "add-book";
  }
  @PostMapping("/addbook/{id}")
	public String addBook(Book book, BindingResult result, Model model,@RequestParam(value = "image",required=true) MultipartFile image,@PathVariable("id") Integer id) {
	   book.setId(id);
	   if(id>0){
			book.setImagePath(bookDAO.findById(id).get().getImagePath());
		}
	   if (result.hasErrors()) {
			return "add-book";
		}
		
		String imageName="fakeimage.png";

		System.out.println(image);

		if(image==null|| image.getSize()==0L){
			
		}else{
			imageName = storageService.store(image);
		}
				
		if(id==0){
			book.setImagePath(imageName);
		}else if(!imageName.equals("fakeimage.png")){
			book.setImagePath(imageName);
		} 
		
		
        book.setUsername(username); 
		bookDAO.save(book);
		model.addAttribute("books", addImagePath(bookDAO.findAllByUsername(this.username)));
		return "redirect:/books";
	}
  
  @GetMapping("/edit/{id}")
	public String showUpdateForm(@PathVariable("id") int id, Model model) {
		Book book = bookDAO.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid book Id:" + id));

		model.addAttribute("book", book);
		model.addAttribute("whenEditForm","");
		return "add-book";
	}
  
  @GetMapping("/delete/{id}")
	public String deleteBook(@PathVariable("id") int id, Model model) {
		Book book = bookDAO.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid book Id:" + id));
		bookDAO.delete(book);
		model.addAttribute("books", addImagePath(bookDAO.findAllByUsername(this.username)));
		return "redirect:/books";
	}
  
  private List<Book> addImagePath(List<Book> books) {
		String contextPath = servletContext.getContextPath();
		System.out.println("contextPath : " + contextPath);
		for (Book book : books) {
			book.setImagePath(contextPath + "/files/" + book.getImagePath());
		}
		return books;
	}
}
