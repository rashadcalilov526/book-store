package com.company.bookstore.controller;

import java.util.ArrayList;
import java.util.List;

import com.company.bookstore.config.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.bookstore.dao.BookDAO;
import com.company.bookstore.dao.CustomerDAO;
import com.company.bookstore.dao.OrderDAO;
import com.company.bookstore.model.BasketBook;
import com.company.bookstore.model.Customer;
import com.company.bookstore.model.OrderModel;



@RestController
@RequestMapping(path="/orders-rest")
@CrossOrigin(origins="*")

public class OrderRestController {
		
	@Autowired
	private OrderDAO orderDAO;
	private String username;
	
	@Autowired
	private CustomerDAO customerDAO;
	
	@Autowired
	private BookDAO bookDAO;
	
	@Autowired
	private MyUserDetails myUserDetails;
	

	@PostMapping (consumes=MediaType.APPLICATION_XML_VALUE,produces=MediaType.APPLICATION_XML_VALUE)
	public List<OrderModel> add(@RequestBody OrderModel orderModel){
		Customer customer =customerDAO.findByPhone(orderModel.getCustomer().getPhone());
		System.out.println(customer);
		if(customer==null){
			
		}else{
			orderModel.setCustomer(customer);
		}
		ArrayList<OrderModel> orders=new ArrayList<OrderModel>();
		
		 // 022-222-2222
		ArrayList<String> usernames=new ArrayList<>();
		int basketBooksSize=orderModel.getBasketBooks().size();
		
		for (int i = 0; i < basketBooksSize ; i++) {
			BasketBook bb=orderModel.getBasketBooks().get(i);
			bb.setBook(bookDAO.findById(bb.getBook().getId()).get());
			String username=bb.getBook().getUsername();
			if(!usernames.contains(username)){
				usernames.add(username);
			}
		}
		
		System.out.println(orderModel);
		System.out.println(usernames);
		for (int i = 0; i < usernames.size(); i++) {
			OrderModel om=new OrderModel();
			om.setCustomer(orderModel.getCustomer());
			om.setUsername(usernames.get(i));
			for (int j = 0; j < orderModel.getBasketBooks().size(); j++) {
				if(orderModel.getBasketBooks().get(j).getBook().getUsername().
						equals(usernames.get(i))){
					om.getBasketBooks().add(orderModel.getBasketBooks().get(j));
				}
			}
			calculateOrderTotalPrice(om);
			this.orderDAO.save(om);
			orders.add(om);
		}
		System.out.println(orders);
		return orders;
	}
	
	public void calculateOrderTotalPrice(OrderModel orderModel){
		  double totalPrice=0;
		  int size=orderModel.getBasketBooks().size();
		  for (int i = 0; i < size; i++) {
			  BasketBook bb=orderModel.getBasketBooks().get(i);
			totalPrice+=bb.getQuantity()*bb.getBook().getPrice();
		}
		  orderModel.setTotalPrice(totalPrice);
		 
	  }
	
	@GetMapping(path="/{username}",produces = MediaType.APPLICATION_XML_VALUE)
	public List<OrderModel> findAllByUsername(@PathVariable(name="username") String username){
			return orderDAO.findAllByUsername(username);
	} 
	
	  
}
