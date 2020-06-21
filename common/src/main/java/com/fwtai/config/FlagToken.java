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
public final class FlagToken{

    private final static ThreadLocal<Integer> flag = new ThreadLocal<Integer>();

    //在拦截器获取并保存token,在需要service或dao层或其他地方调用解析即可
    /**
        1.是该更换token了呢;<br/>
        2.无效的token;<br/>
        3.需要重新登录;<br/>
     */
    public static Integer get(){
        return flag.get();
    }

    public static void set(final Integer value){
        flag.set(value);
    }

    public static void remove(){
        flag.remove();
    }
}