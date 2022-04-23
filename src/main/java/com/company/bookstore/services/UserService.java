package com.company.bookstore.services;

import com.company.bookstore.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {
User save(UserRegistrationDto registrationDto);

    UserDetails loadUserByUserName(String username)throws UsernameNotFoundException;
}
