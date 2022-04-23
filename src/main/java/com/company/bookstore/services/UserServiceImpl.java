package com.company.bookstore.services;

import com.company.bookstore.dao.UserRepository;
import com.company.bookstore.model.Role;
import com.company.bookstore.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{
   @Autowired
   private UserRepository userRepository;

   @Autowired
   private BCryptPasswordEncoder bCryptPasswordEncoder;
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public User save(UserRegistrationDto registrationDto) {
       User user=new User(registrationDto.getUsername(),bCryptPasswordEncoder.encode(registrationDto.getPassword()), Arrays.asList(new Role("USER")));
    return userRepository.save(user);
    }
    @Override
    public UserDetails loadUserByUserName(String username)throws UsernameNotFoundException{
    User user =userRepository.findByUsername(username);
    if (user==null){
        throw new UsernameNotFoundException("Invalid username or password");
    }
    return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),mapRolesToAuthorities(user.getRoles()));
    }
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());

    }
}
