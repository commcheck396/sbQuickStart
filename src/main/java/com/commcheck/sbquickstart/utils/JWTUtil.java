package com.commcheck.sbquickstart.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtil {
    public static String JWTGeneration(Map<String, Object> claims) {
        String token = JWT.create().withClaim("user", claims)
                .withExpiresAt(new Date(System.currentTimeMillis()+1000*60*60*120))
                .sign(Algorithm.HMAC256("commcheck"));
        return token;
    }
    public static Map<String, Object> JWTVerification(String token) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("commcheck")).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        Map<String, Object> claims = decodedJWT.getClaim("user").asMap();
        System.out.println(claims);
        return claims;
    }
}
