package com.psicolApp.apirest.utils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import com.psicolApp.apirest.etities.Patient;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    public String getToken(UserDetails user) {
        return getToken(new HashMap<>(), user);
    }

    private String getToken(Map<String,Object> extraClaims, UserDetails user){
        return Jwts.
        builder()
        .claims(extraClaims)
        .claim("username",  user.getUsername())
        .claim("role", user.getAuthorities())
        .subject(user.getUsername())
        .issuedAt(new Date(System.currentTimeMillis()+100))
        .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24) )
        .signWith(getKey())
        .compact();

    }

    private SecretKey getKey() {
        byte [] KeyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(KeyBytes);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {

        final String username=getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername())&& !isTokenExpired(token));
        
        
       
    }

    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    private Claims getAllClaims(String token){

        return Jwts
        .parser()
        .verifyWith(getKey()) //Se obtiene la clave
        .build()
        .parseSignedClaims(token) // Se parsea 
        .getPayload(); // Se obtiene el payload

    }
    public <T> T getClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpiration(String token)
    {
        return getClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token)
    {
        return getExpiration(token).before(new Date());
    }
}
