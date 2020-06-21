package com.fwtai.auth;

import com.fwtai.config.FlagToken;
import com.fwtai.config.RenewalToken;
import com.fwtai.tool.ToolClient;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 身份校验失败处理器，如 token 错误
*/
@Component
public class AuthenticationPointHandler implements AuthenticationEntryPoint{

    @Override
    public void commence(final HttpServletRequest request,final HttpServletResponse response,final AuthenticationException exception) throws IOException, ServletException{
        final Integer flag = FlagToken.get();//值若为空说明是未带token且未放行的url而请求导致的
        System.out.println("flag-->"+flag);
        String json = ToolClient.notAuthorized();
        if(flag != null){
            switch (flag){
                case 1:
                    //json = ToolClient.createJsonFail("更换access_token");
                    System.out.println("更换access_token"+ RenewalToken.get());
                    break;
                case 2:
                    System.out.println("无效的refresh_token"+ RenewalToken.get());
                    json = ToolClient.tokenInvalid();
                    break;
                case 3:
                    json = ToolClient.tokenInvalid();
                    break;
                default:
                    break;
            }
        }
        System.out.println("++++++++++++++"+exception.getMessage()+"++++++++++++++");
        ToolClient.responseJson(json,response);
        // todo 处理 ThreadLocal
    }
}