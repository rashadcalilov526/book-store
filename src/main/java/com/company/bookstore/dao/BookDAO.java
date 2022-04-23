package com.company.bookstore.dao;

import java.util.List;

import com.company.bookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookDAO extends JpaRepository<Book, Integer>{
	public List<Book> findAllByUsername(String username);
	
	@Query(value="select * from book where name like %?1%",nativeQuery=true)
	public List<Book> findAllSearch(String search);
}
