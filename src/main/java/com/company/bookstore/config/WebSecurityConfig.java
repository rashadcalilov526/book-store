package com.company.bookstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsServiceImpl();
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
       authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception{
        authenticationManagerBuilder.authenticationProvider(authenticationProvider());
    }
    protected void configure(HttpSecurity http)throws Exception{

        http.authorizeRequests()
                .antMatchers(HttpMethod.GET,"/").permitAll()
                .antMatchers(HttpMethod.GET,"/home").permitAll()
                .antMatchers(HttpMethod.GET,"/files/**").permitAll()
                .antMatchers(HttpMethod.GET,"/createUserShowForm").permitAll()
                .antMatchers(HttpMethod.POST,"/createUserProcess").permitAll()
                .antMatchers(HttpMethod.GET,"/customer").permitAll()
                .antMatchers(HttpMethod.GET,"/orders-rest/**").permitAll()
                .antMatchers(HttpMethod.POST,"/orders-rest").permitAll()
                .antMatchers(HttpMethod.GET,"/customer-rest/find-all").permitAll()
                .antMatchers(HttpMethod.GET,"/customer-rest/search/**").permitAll()
                .antMatchers(HttpMethod.GET,"/customer/confirmation-form").permitAll()
                .antMatchers(HttpMethod.GET,"/order-confirmation-message").permitAll()
                .antMatchers("/registration**").permitAll()
                .antMatchers(HttpMethod.GET,"/basket").permitAll()
                .antMatchers(HttpMethod.OPTIONS,"/**").permitAll()

                .anyRequest().authenticated().and().formLogin().loginPage("/login").usernameParameter("username").passwordParameter("password")
                .permitAll().and().logout().permitAll().and().exceptionHandling().accessDeniedPage("/403");
                http.csrf().disable();
                http.headers().frameOptions().disable();
    }
}
