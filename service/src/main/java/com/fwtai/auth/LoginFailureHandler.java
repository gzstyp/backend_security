package com.fwtai.auth;

import com.fwtai.tool.ToolClient;
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
        final String msg = "用户名或密码错误:" + e.getMessage();
        final String json = ToolClient.exceptionJson(msg);
        ToolClient.responseJson(json,response);
    }
}