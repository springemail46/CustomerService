package com.spring.auth.service;

import com.spring.auth.entity.UserData;
import com.spring.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserData user = repository.findByUsername(username);
        List<SimpleGrantedAuthority> roles=null;
        if(username.equals(user.getUsername()) && user.getRole().equals("admin")){
            roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
            return new User(user.getUsername(), user.getPassword(), roles);
        }else if(username.equals(user.getUsername()) && user.getRole().equals("user")){
            roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
            return new User(user.getUsername(), user.getRole(), roles);
        }
        throw new UsernameNotFoundException("User is not available for this username"+username);
    }
    public UserData saveUser(UserData userData){
        UserData user = new UserData();
        user.setUsername(userData.getUsername());
        user.setPassword(bcryptEncoder.encode(userData.getPassword()));
        user.setRole(userData.getRole());
        return repository.save(user);
    }
}
