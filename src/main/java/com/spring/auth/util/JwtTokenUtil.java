package com.spring.auth.util;


import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

@Service
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secret;

    private int jwtExpirationInMs;

    @Value("${jwt.secret}")
    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Value("${jwt.expirationDateInMs}")
    public void setJwtExpirationInMs(int jwtExpirationInMs) {
        this.jwtExpirationInMs = jwtExpirationInMs;
    }

    public String getUsernameFromToken(String token){
        return getClaimFromToken(token, Claims ::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver){
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
    public List<SimpleGrantedAuthority> getRolesFromToken(String token){
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        List<SimpleGrantedAuthority> roles = null;
        Boolean isAdmin = claims.get("isAdmin", Boolean.class);
        Boolean isUser  = claims.get("isUser", Boolean.class);
        if(isAdmin != null && isAdmin){
            roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        if(isUser != null && isAdmin){
            roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
        }
        if(isUser != null && isUser){
            roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return roles;
    }
    /*private Boolean ignoreTokenExpiration(String token){
        return false;
    }*/
    public String  generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
       Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();
       if(roles.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
           claims.put("isAdmin", true);
       }
       if(roles.contains(new SimpleGrantedAuthority("ROLE_USER"))){
           claims.put("isUser", true);
       }
       return doGenerateToken(claims, userDetails.getUsername());
    }
    private String doGenerateToken(Map<String, Object> claims, String subject){
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ jwtExpirationInMs)).signWith(SignatureAlgorithm.HS512, secret).compact();
    }
    /*public Boolean canTokenBeRefreshed(String token){
        return (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }*/
    public Boolean validateToken(String token){
        try{
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        }catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex){
            throw new BadCredentialsException("INVALID_CREDENTIALS",ex);
        }catch (ExpiredJwtException ex){
            throw ex;
        }
    }

}
