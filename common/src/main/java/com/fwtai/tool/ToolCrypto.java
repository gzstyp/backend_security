package com.fwtai.tool;

import sun.misc.BASE64Encoder;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;

/**
 * 非对称加密解密
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-04-24 23:40
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
 */
public final class ToolCrypto{

    private final static String PUBLIC_KEY = "RSAPublicKey";
    private final static String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * 生成一对公钥私钥
     * @param keysize 生成密钥位数,密钥长度 512-65536 & 64的倍数,一般的有512、1024、2048
    */
    public final static HashMap<String,Object> initKey(final int keysize){
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(keysize);
            //通过对象 KeyPairGenerator 获取对象KeyPair
            final KeyPair keyPair = keyPairGen.generateKeyPair();
            //通过对象 KeyPair 获取RSA公私钥对象RSAPublicKey RSAPrivateKey
            final RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            final RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            //公私钥对象存入map中
            final HashMap<String, Object> keyMap = new HashMap<String, Object>();
            keyMap.put(PUBLIC_KEY,publicKey);
            keyMap.put(PRIVATE_KEY,privateKey);
            return keyMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //从方法initKey()获得公钥
    public final static String getPublicKey(final HashMap<String, Object> keyMap) {
        //获得map中的公钥对象 转为key对象
        final Key key = (Key) keyMap.get(PUBLIC_KEY);
        //byte[] publicKey = key.getEncoded();
        //编码返回字符串
        return (new BASE64Encoder()).encodeBuffer(key.getEncoded());
    }

    //从方法initKey()获得私钥
    public final static String getPrivateKey(final HashMap<String, Object> keyMap)  {
        //获得map中的私钥对象 转为key对象
        final Key key = (Key) keyMap.get(PRIVATE_KEY);
        //byte[] privateKey = key.getEncoded();
        //编码返回字符串
        return (new BASE64Encoder()).encodeBuffer(key.getEncoded());
    }

    public static void main(String[] args){
        HashMap<String,Object> keyMap = initKey(2048);
        final String publicKey = getPublicKey(keyMap);
        final String privateKey = getPrivateKey(keyMap);
        System.out.println(publicKey);
        System.out.println("---------------上面的公钥,下面是私钥----------------");
        System.out.println(privateKey);
    }
}