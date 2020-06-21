package com.fwtai.auth;

import com.fwtai.config.FlagToken;
import com.fwtai.config.RenewalToken;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 菜单|权限拦截器[认证和权限拦截应该分开,各执其职]
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-02-23 18:22
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
public final class AuthInterceptor implements HandlerInterceptor{

    @Override
    public void afterCompletion(final HttpServletRequest request,final HttpServletResponse response,final Object handler,final Exception exception) throws Exception{
        FlagToken.remove();
        RenewalToken.remove();
        System.out.println("*************************afterCompletion*************************");
    }
}