package com.commcheck.sbquickstart;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {
    @Test
    public void testJWTGeneration() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", "123");
        claims.put("username", "John Doe");
        String token = JWT.create().withClaim("user", claims)
                .withExpiresAt(new Date(System.currentTimeMillis()+1000*60*60*12))
                .sign(Algorithm.HMAC256("commcheck"));
//        System.out.println(token);
    }

    @Test
    public void testJWTVerification() {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoiMTIzIiwidXNlcm5hbWUiOiJKb2huIERvZSJ9LCJleHAiOjE3MTAxNzAxNDF9.onivc0xw1i9X9xxSOR10nS3qL95NOdEyX06rmzpI7fc";
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("commcheck")).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        Map<String, Claim> claims = decodedJWT.getClaims();
//        System.out.println(claims.get("user"));
    }
}
