package com.example.security.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JwtService {

    //https://www.allkeysgenerator.com/
    //256 bits
    private static final String SECTET_KEY = "68566D597133743677397A244226452948404D635166546A576E5A7234753778";

    // map: everything I want to pass into token
    public String generateToken(Map<String, Object> claimMap, UserDetails userDetails){
        return Jwts.builder().setClaims(claimMap)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis())) // when created, cal. expiration date/ check if token is valid
                .setExpiration(new Date(System.currentTimeMillis() + 24* 60 * 1000)) // when to expire
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) //use the HS256
                .compact();
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        //check if token belongs to userDetails' token, check time of token
        final String tokenUserName = extractUsername(token);
        return tokenUserName.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expirationDate = extractClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }

    public String extractUsername(String jwtToken) { //email in our application
        return extractClaim(jwtToken, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T>resolver){ //<input, ouput>
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims); //apply: call this function with parameter claims
    }
    // return payload(body) part of jwt token
    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECTET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
