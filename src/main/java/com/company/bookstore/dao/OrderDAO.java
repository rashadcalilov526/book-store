package com.company.bookstore.dao;

import java.util.List;

import com.company.bookstore.model.OrderModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDAO extends JpaRepository<OrderModel, Integer>{
 public List<OrderModel> findAllByUsername(String username);
}
