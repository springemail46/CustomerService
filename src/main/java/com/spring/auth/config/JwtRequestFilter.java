package com.spring.auth.config;

import com.spring.auth.service.JwtUserDetailsService;
import com.spring.auth.util.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
           try{
               String jwtToken = extractJwtFromRequest(request);
               if(StringUtils.hasText(jwtToken) && jwtTokenUtil.validateToken(jwtToken)){
                   UserDetails userDetails = new User(jwtTokenUtil.getUsernameFromToken(jwtToken), "",jwtTokenUtil.getRolesFromToken(jwtToken));
                   UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                   SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
               }else {
                   System.out.println("Can not set the security context ");
               }
           }catch (ExpiredJwtException ex){
               request.setAttribute("exception", ex);
           }catch (BadCredentialsException ex){
               request.setAttribute("exception", ex);
           }
           filterChain.doFilter(request, response);
    }
    private String extractJwtFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
