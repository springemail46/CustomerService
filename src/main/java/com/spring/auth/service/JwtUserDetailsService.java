package com.spring.auth.service;

import com.netflix.discovery.converters.Auto;
import com.spring.auth.entity.UserData;
import com.spring.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserData user = repository.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("User not found with username "+username);
        }
        return new User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }
    public UserData saveUser(UserData userData){
        UserData user = new UserData();
        user.setUsername(userData.getUsername());
        user.setPassword(bcryptEncoder.encode(userData.getPassword()));
        return repository.save(user);
    }
}
