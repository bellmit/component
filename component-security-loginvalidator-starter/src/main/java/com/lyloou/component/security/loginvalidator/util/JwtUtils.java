package com.lyloou.component.security.loginvalidator.util;

import com.lyloou.component.security.loginvalidator.exception.ValidateLoginException;
import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

/**
 * jwt 工具类
 *
 * @author lilou
 */
public class JwtUtils {

    /**
     * 解析 jwt token
     *
     * @param token     需要解析的json
     * @param secretKey 密钥
     * @return token 内容
     */
    public static Jws<Claims> parserJwtToken(String token, String secretKey) {

        try {
            //We will sign our JWT with our ApiKey secret
            byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
            SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
            Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
            return Jwts.parser()
                    .setSigningKey(signingKey)
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new ValidateLoginException("token参数已过期");
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | IncorrectClaimException e) {
            throw new ValidateLoginException("token参数无效");
        }
    }

    /**
     * 判断 jwt 是否过期
     *
     * @param jws token 内容
     * @return true:过期 false:没过期
     */
    public static boolean isExpired(Jws<Claims> jws) {
        return jws.getBody().getExpiration().before(new Date());
    }


    /**
     * 生成 jwt token
     */
    public static String createJwtToken(Map<String, Object> claimsMap, Long expireSecond, String secretKey) {
        Date expireTime = Date.from(LocalDateTime.now().plusSeconds(expireSecond).atZone(ZoneId.systemDefault()).toInstant());

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setIssuedAt(now)
                .setExpiration(expireTime)
                .addClaims(claimsMap)
                .signWith(signatureAlgorithm, signingKey);

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }
}
