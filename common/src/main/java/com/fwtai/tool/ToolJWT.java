package com.fwtai.tool;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;

import java.io.Serializable;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

/**
 * jwt(JSON Web Token)令牌工具类,采用非对称公钥密钥
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-02-12 23:53
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
@Component
public class ToolJWT implements Serializable{

    //如设置Token过期时间15分钟，建议更换时间设置为Token前5分钟,通过try catch 获取过期
    private final long access_token = 1000 * 60 * 15;//当 refresh_token 已过期了，再判断 access_token 是否已过期,

    /**一般更换新的access_token小于5分钟则提示需要更换新的access_token*/
    private final long refresh_token = 1000 * 60 * 10;//仅做token的是否需要更换新的access_token标识,小于5分钟则提示需要更换新的access_token

    private String issuer = "贵州富翁泰科技有限责任公司";//jwt签发者

    /**2048的密钥位的公钥*/
    private String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAh3ewCTu1AMYu1sT2gW80oFBz4Je1cE20RJ1rh2ZUd9iifunHhnIlgtQUADaZDtth7sZvhGBls5oCHzWIujMWV0TXhjFdmk2r12oHqcdKGRSbqDdaJjMLqn3Uz/ySEHsQMRAoRFjZTc9UVlMltaGfubNCPdqBFPrIUHvvCvqbrE8dO3ObA0VOaEuQ6F5EBuSG3vyGRLkOb07+OryFBXj9JdF//8N8KzDsdSy8D6dQUSt7ntR8J1xIDZdRKNJJpVhdfPMi3+/X61H8iP5YC9nx0+0ULkZZDgA0rIFzoU5dXFH0GV9rhi6i8u9+4e8gP1b3zmcWXoS58nUna0p5hj8ujwIDAQAB";
    /**2048的密钥位的私钥*/
    private String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCHd7AJO7UAxi7WxPaBbzSgUHPgl7VwTbREnWuHZlR32KJ+6ceGciWC1BQANpkO22Huxm+EYGWzmgIfNYi6MxZXRNeGMV2aTavXagepx0oZFJuoN1omMwuqfdTP/JIQexAxEChEWNlNz1RWUyW1oZ+5s0I92oEU+shQe+8K+pusTx07c5sDRU5oS5DoXkQG5Ibe/IZEuQ5vTv46vIUFeP0l0X//w3wrMOx1LLwPp1BRK3ue1HwnXEgNl1Eo0kmlWF188yLf79frUfyI/lgL2fHT7RQuRlkOADSsgXOhTl1cUfQZX2uGLqLy737h7yA/VvfOZxZehLnydSdrSnmGPy6PAgMBAAECggEAH4SpYHyT0GpL24xYDiVhiSsuysKBZG+v0YcOzaHxZTDyHbUmxxEnyRiuzp3lXp+MWZGxwIrtHqxmcfxyo1/fgs5xlrdFn/ESWjxBLC9B/jPdQ3Ydc2XNAQ3lxb5t/YekbMXlmIFTjdb/OFaAH5JLJ1mdv+ZmwgrXMGKla1iDf8NPHWxoPPdye2vUHoPPO9+VZazJkOYWQp7Gl7cEjSJy8rltAo3te6RMuVXAuNUdvyivm1FBM22nOxPKTPC/si5QOYCzTLCjLmJYqBcvPr93HXSsyh0xi3DAltmjHLCOMpBQjxasK1T1ULgIhRi1nzCJwox5N/jrF0UCPfO7GLFbgQKBgQDu7PQLtzwZ1S4Id+wPlpdKDsk29kBrSthiZivnQcHGPtf0JlxGky1SmLKeAjmszSIBltGc9qIjXIU+9x/DfxMNZRTg63xTr9toxztU7bRU6VfkKA2setKaTw1jlFU0qQwcALQiAasPcjkyaKg6bDNHCkNkytdsJX7FdbEgu2IkiwKBgQCRJgLSkIpsZo1J8cpaDTLkFCBACKJsMljnvgpdww+XQHzol6zIXwkaCQb6FJVI6tAC9ok+NIHSWIH/YMo4Mu34HxsFShOekvE4hISRY74X/5sxqAjugwWk+4CFVXDIeuLwA5hLXUaWXMLE3bjiMzQx7FQeBuGiahtLE5Wz6p7qjQKBgQCSm0ds4PS6DTt/6sYpEoim4sfJN/VzYKvCRVtvPcQ/d1Rf9iHtFGZdJmGD322wgPb67qaUoCoBdMY2SsFs3k68i2fyU80oNOJ3OrlHdcyPxdcuov0w9vS/xv46OkzGUWyiyjO+IjPq+HXsXpfLsZUNZFjSQj1JmQBe/cbAhPrF3QKBgGsXEWQL0qVx8HKLG5HfRRn0I2s7M6MCbofktb9B6KHeqYnuRkO7onp8CJLAVvhqjrhw7wdfNB1eweMKpYhbQyoawRPg+KK5pZHea/TdE2afZwB3Csf3EVYNXWCCjmjMCd2tuVJ5algL7oPTVtJFlw/yXomc6CKtiJ+Wvo/03fUNAoGAH9tknQuM+wq+s25q7ypExfPYyXarC/Jr2w4FQQXiz7d3tPhrxagR1S8ZaH2sIbZqpxscvvfnYDVtIeCxxmZpawEhJ7SruT9dYlGMmA6sFs0+D7naPo5CQk89BXzkAmJadgDzwT6f3LQ+D38BkVv64h/CiJmOPAa8Gwu+E5U3zW0=";

    /**java生成的私钥是pkcs8格式的公钥是x.509格式*/
    private PublicKey getPublicKey(){
        try {
            final byte[] keyBytes = (new BASE64Decoder()).decodeBuffer(publicKey);
            final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            final PublicKey publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private PrivateKey getPrivateKey(){
        try {
            final byte[] keyBytes = (new BASE64Decoder()).decodeBuffer(privateKey);
            final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            final PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            return privateKey;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // setSubject 不能和s etClaims() 同时使用,如果用不到 userId() 的话可以把setId的值设为 userName !!!
    private String createToken(final String userId,final Object value,final long expiryDate){
        final ExecutorService threadPool = Executors.newCachedThreadPool();
        final Future<String> future = threadPool.submit(new Callable<String>(){
            @Override
            public String call() throws Exception{
                final long date = System.currentTimeMillis();
                final JwtBuilder builder = Jwts.builder().signWith(getPrivateKey(),SignatureAlgorithm.RS384);
                if(value !=null){
                    builder.claim(userId,value);
                }
                return builder.setId(userId).setIssuer(issuer).setIssuedAt(new Date(date)).setExpiration(new Date(date + expiryDate)).compact();
            }
        });
        try {
            return future.get();
        } catch (final Exception e) {
            threadPool.shutdown();
            return null;
        }
    }

    public Claims parser(final String token){
        final JwtParserBuilder builder = Jwts.parserBuilder();
        return builder.requireIssuer(issuer).setSigningKey(getPublicKey()).build().parseClaimsJws(token).getBody();
    }

    /**
     * 验证token是否已失效,返回true已失效,否则有效
     * @param token
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2020年2月24日 16:19:00
    */
    public boolean tokenExpired(final String token) {
        try {
            return parser(token).getExpiration().before(new Date());
        } catch (final ExpiredJwtException exp) {
            return true;
        }
    }

    public boolean validateToken(final String token,final String userId){
        final String uid = extractUserId(token);
        return (uid.equals(userId) && !tokenExpired(token));
    }

    /**仅作为是否需要刷新的access_token标识,不做任何业务处理*/
    public String expireRefreshToken(final String userId){
        return createToken(userId,null,refresh_token);
    }

    /**生成带认证实体且有权限的token,最后个参数是含List<String>的角色信息,*/
    public String expireAccessToken(final String userId){
        return createToken(userId,null,access_token);
    }

    private <T> T extractObjet(final String token,final Function<Claims,T> claimsResolver){
        final Claims claims = parser(token);
        return claimsResolver.apply(claims);
    }

    public String extractUserId(final String token){
        return extractObjet(token,Claims::getId);
    }
}