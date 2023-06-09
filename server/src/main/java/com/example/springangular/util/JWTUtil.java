package com.example.springangular.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JWTUtil {
    @Value("${jwt_secret_key}")
    private String secretKey;

    public String generateToken(String username){
        Date expireDate = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());

        return JWT.create().withSubject("Person details")
                .withIssuer("Spring-security")
                .withIssuedAt(new Date())
                .withExpiresAt(expireDate)
                .withClaim("username", username)
                .sign(Algorithm.HMAC256(secretKey));
    }

    public String validateTokenAndRetrieveClaim(String token){
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey))
                .withSubject("Person details")
                .withIssuer("Spring-security")
                .build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getClaim("username").asString();
    }
}
