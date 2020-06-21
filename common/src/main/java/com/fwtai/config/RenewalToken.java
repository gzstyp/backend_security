package com.fwtai.config;

/**
 * 保存高并发环境下的用户刷新token信息
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-04-09 16:36
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
public final class RenewalToken{

    private final static ThreadLocal<String> localRenewal = new ThreadLocal<String>();

    //在拦截器获取并保存token,在需要service或dao层或其他地方调用解析即可
    public static String get(){
        return localRenewal.get();
    }

    public static void set(final String value){
        localRenewal.set(value);
    }

    public static void remove(){
        localRenewal.remove();
    }
}