package com.vip.file.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zkc
 * @date 2021-06-18 11:37
 * 获取token
 */
@Slf4j
public class TokenUtils {
    /**
     * 设置过期时间
     *
     */
    private static final long EXPIRE_DATE = 180 * 60 * 60 * 1000;
    /**
     * token秘钥
     */
    private static final String TOKEN_SECRET = "ZhongGuanTechnology2021RMC";

    public static String getToken(String username, String password) {
        String token = "";
        try {
            //过期时间
            Date date = new Date(System.currentTimeMillis() + EXPIRE_DATE);
            //秘钥及加密算法
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            //设置头部信息
            Map<String, Object> header = new HashMap<>(2);
            header.put("typ", "JWT");
            header.put("alg", "HS256");
            //携带username，password信息，生成签名
            token = JWT.create()
                    .withHeader(header)
                    .withClaim("userName", username)
                    .withClaim("passWord", password).withExpiresAt(date)
                    .sign(algorithm);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return token;
    }
    /**
     * @desc 验证token，通过返回true
     * @params [token]需要校验的串
     **/
    public static String verify(String token){
            String userName="";
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = null;
        try {
            jwt = verifier.verify(token);
        } catch (JWTVerificationException e) {

            return userName;
        }
        userName = jwt.getClaims().get("userName").asString();
        return userName;

    }

    /**
     * 从token中获取username信息,无需解密
     * @param token
     * @return
     */
    public static String getUserName(String token){
        try {
            DecodedJWT jwt = JWT.decode(token);
            if(System.currentTimeMillis()-jwt.getExpiresAt().getTime()>0){
                return null;
            }
            return jwt.getClaim("userName").asString();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
