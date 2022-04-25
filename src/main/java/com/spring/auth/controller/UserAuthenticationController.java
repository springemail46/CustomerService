package com.spring.auth.controller;

import com.spring.auth.entity.AuthenticationRequest;
import com.spring.auth.entity.AuthenticationResponse;
import com.spring.auth.entity.UserData;
import com.spring.auth.service.JwtUserDetailsService;
import com.spring.auth.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService service;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest request) throws Exception{
        authenticate(request.getUsername(), request.getPassword());
        final UserDetails userDetails = service.loadUserByUsername(request.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public ResponseEntity<?> saveUser(@RequestBody UserData userData) throws Exception{
        return ResponseEntity.ok(service.saveUser(userData));
    }
    private void authenticate(String username, String password) throws Exception{
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        }catch (DisabledException ex){
            throw new Exception("USER_DISABLED", ex);
        }catch (BadCredentialsException ex){
            throw new Exception("INVALID_CREDENTIALS", ex);
        }
    }
}
