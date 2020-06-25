package com.fwtai.auth;

import com.fwtai.entity.User;
import com.fwtai.service.web.UserService;
import com.fwtai.tool.ToolClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * 登录认证过滤器,在此处理锁定功能!!!
*/
public class LoginAuthFilter extends UsernamePasswordAuthenticationFilter{

    @Autowired
    private UserService userService;

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request,final HttpServletResponse response) throws AuthenticationException{
        final HashMap<String,String> params = ToolClient.getFormParams(request);
        final String p_username = "username";
        final String p_password = "password";
        final String validate = ToolClient.validateField(params,p_username,p_password);
        if(validate != null){
            ToolClient.responseJson(validate,response);
            return null;
            //throw new AuthExceptionHandler("请求参数不完整");
        }
        final String username = params.get(p_username);
        final String password = params.get(p_password);
        if(userService.checkLogin(username,password)){
            //将账号、密码装入UsernamePasswordAuthenticationToken中,即这个方法是没有角色或权限,只是单纯的保存用户名和密码
            final UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username,password);// 这个方法是没有角色或权限
            setDetails(request,authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        }else{
            //在此处理锁定功能!!!
            final User user = userService.queryUser(username);
            if(user != null){
                final Integer errorCount = user.getErrorCount() + 1;
                if(errorCount < 4){
                    userService.updateErrors(username);
                }
                final Long error = user.getError();
                if(error < 0){
                    final String json = "当前帐号或密码连续错误3次<br/>已被系统临时锁定<br/>请在"+user.getErrorTime()+"后重新登录";
                    ToolClient.responseJson(ToolClient.createJsonFail(json),response);
                    return null;
                }
                if (errorCount >= 3){
                    userService.updateLoginTime(username);//当错误3次时更新错误的时刻就锁定
                    final String json = "当前帐号或密码连续错误3次<br/>已被系统临时锁定,请30分钟后重试";
                    ToolClient.responseJson(ToolClient.createJsonFail(json),response);
                    return null;
                }
            }
            ToolClient.responseJson(ToolClient.invalidUserInfo(),response);
            return null;
            //return this.getAuthenticationManager().authenticate(null);//不能用这个，否则报错 NullPointerException
            //throw new AuthExceptionHandler("用户名或密码错误");
        }
    }
}