package com.henrickdaniel.tokengenerator.security;

import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtGenerator {

    private static final SecretKey key = Jwts.SIG.HS256.key().build();


    public String generateToken(Authentication authentication){
        String userName = authentication.getName();
        Date currentDate = new Date();

        LocalDateTime expireDate = LocalDateTime.now();
        expireDate = expireDate.plusDays(1);


        return Jwts.builder().subject(userName).issuedAt(currentDate).expiration(converterToDate(expireDate)).signWith(JwtGenerator.key).compact();
    }

    public String getUserNameFromJwt(String token){

        return Jwts.parser().verifyWith(JwtGenerator.key).build().parseSignedClaims(token).getPayload().getSubject();

    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(JwtGenerator.key).build().parseSignedClaims(token);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect");
        }
    }

    private static Date converterToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

}
