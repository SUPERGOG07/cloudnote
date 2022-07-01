package com.superdog.cloudnote.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;

import java.util.Calendar;

import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    private static String SECRET = "!sdiua489";

    private static String ISSUER = "superdog";

    public static String createToken(Map<String,String> claims) throws JWTCreationException {

        //头部header
        Map<String,Object> header = new HashMap<>();
        header.put("alg","HS256");
        header.put("typ","JWT");

        //发行时间和过期时间
        Calendar instance = Calendar.getInstance();
        Calendar expires = instance;
        expires.add(Calendar.DATE,7);

        //payload以及secret
        JWTCreator.Builder token = JWT.create()
                .withHeader(header)                 //头
                .withIssuer(ISSUER)                 //发行人
                //.withClaim()                      //声明信息
                .withSubject("LOGIN_TOKEN")         //主题
                .withAudience()                     //受众
                .withIssuedAt(instance.getTime())   //发行时间
                .withExpiresAt(expires.getTime());

        claims.forEach(token::withClaim);
        return token.sign(Algorithm.HMAC256(SECRET));
    }

    public static Map<String , Claim> decodeToken(String token) throws JWTVerificationException {
        //解码器
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).withIssuer(ISSUER).build();
        //返回解码后声明map
        return verifier.verify(token).getClaims();
    }
}
