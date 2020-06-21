package com.fwtai.auth;

import com.fwtai.tool.ToolClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 注销成功退出处理器
*/
@Component
public class LogoutSuccessService implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request,HttpServletResponse response,Authentication authentication) throws IOException, ServletException{
        final String json = ToolClient.createJsonSuccess("退出注销成功");
        ToolClient.responseJson(json,response);
    }
}