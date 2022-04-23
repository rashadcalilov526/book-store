package com.company.bookstore.dao;

import com.company.bookstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    @Query("select u from User u where u.username= :username")
    public  User getUserByUsername(@Param("username") String username);

    User findByUsername(String username);
}
