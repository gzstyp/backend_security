package com.fwtai.auth;

import com.fwtai.tool.ToolClient;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录失败操作
*/
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler{

    @Override
    public void onAuthenticationFailure(final HttpServletRequest request,final HttpServletResponse response,final AuthenticationException e){
        String msg = "登录失败,原因:" + e.getMessage();
        if(e instanceof LockedException){
            msg = "账号已被禁用冻结";
        }else if(e instanceof BadCredentialsException){
            msg = "账号或密码错误";
        }else if(e instanceof AccountExpiredException){
            msg = "账号已过期失效";
        }else if(e instanceof DisabledException){
            msg = "账号已被禁用冻结";
        }
        ToolClient.responseJson(ToolClient.exceptionJson(msg),response);
    }
}