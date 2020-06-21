package com.fwtai.auth;

import com.fwtai.tool.ToolClient;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 权限校验拒绝处理器(拒绝访问)
*/
@Component
public class AccessDeniedService implements AccessDeniedHandler{

    @Override
    public void handle(HttpServletRequest request,HttpServletResponse response,AccessDeniedException accessDeniedException) throws IOException, ServletException{
        final String msg = "权限不足:" + accessDeniedException.getMessage();
        final String json = ToolClient.exceptionJson(msg);
        ToolClient.responseJson(json,response);
    }
}