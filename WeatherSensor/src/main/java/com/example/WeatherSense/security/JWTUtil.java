package com.example.WeatherSense.security;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

import com.auth0.jwt.JWT;



@Component
public class JWTUtil {
    @Value("${jwt_secret}")
    private String secret;

    @Value("${jwt_subject}")
    private String subject;

    @Value("${jwt_issuer}")
    private String issuer;

    public String generateToken(String username){
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());
        return JWT.create()
                .withSubject(subject)
                .withClaim("username",username)
                .withIssuedAt(new Date())
                .withIssuer(issuer)
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateTokenAndRetrieveClaim(String token){
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject(subject)
                .withIssuer(issuer)
                .build();

        DecodedJWT decodedJWT = verifier.verify(token);

        return decodedJWT.getClaim("username").asString();
    }

}
