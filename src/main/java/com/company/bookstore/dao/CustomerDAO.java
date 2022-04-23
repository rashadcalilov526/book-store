package com.company.bookstore.dao;

import com.company.bookstore.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerDAO extends JpaRepository<Customer, Integer>{
	public  Customer  findByPhone(String phone);
}
