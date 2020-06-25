package com.fwtai.auth;

import com.fwtai.config.ConfigFile;
import com.fwtai.config.FlagToken;
import com.fwtai.config.LocalUserId;
import com.fwtai.config.RenewalToken;
import com.fwtai.service.UserServiceDetails;
import com.fwtai.tool.ToolJWT;
import com.fwtai.tool.ToolString;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token拦截器(最先请求的拦截器)||更换token在这里实现[用于每次外部对接口请求时的Token处理,其中 Once 是每一次,pre是 预先]
*/
@Component
public class RequestFilter extends OncePerRequestFilter {

    @Resource
    private UserServiceDetails userDetailsService;

    @Autowired
    private ToolJWT toolToken;

    @Override
    protected void doFilterInternal(final HttpServletRequest request,final HttpServletResponse response,final FilterChain chain) throws ServletException, IOException {
        final String uri = request.getRequestURI();
        System.out.println("请求的uri-->"+uri);//访问两次很正常
        /*final String[] urls = ConfigFile.IGNORE_URLS;
        for(int x = 0; x < urls.length; x++){
            final String url = urls[x];
            if(uri.equals(url)){
                //response.setHeader("Access-Control-Allow-Origin","*");//登录时报错这个,是因为把登录的url添加到放行的url导致的
                chain.doFilter(request,response);
                return;
            }
        }*/
        final String refresh_token = ToolString.wipeString(request.getHeader(ConfigFile.REFRESH_TOKEN));
        final String access_token = ToolString.wipeString(request.getHeader(ConfigFile.ACCESS_TOKEN));
        final String url_refresh_token = ToolString.wipeString(request.getParameter(ConfigFile.REFRESH_TOKEN));
        final String url_access_token = ToolString.wipeString(request.getParameter(ConfigFile.ACCESS_TOKEN));
        final String refresh = refresh_token == null ? url_refresh_token : refresh_token;
        final String access = access_token == null ? url_access_token : access_token;

        //final String refreshToken = request.getHeader(ConfigFile.refreshToken);
        //final String token = request.getHeader(header);
        if(refresh != null && access != null){
            //判断令牌是否过期，默认是一周,比较好的解决方案是：
            //登录成功获得token后，将token存储到数据库（redis）
            //将数据库版本的token设置过期时间为15~30分钟
            //如果数据库中的token版本过期，重新刷新获取新的token
            //注意：刷新获得新token是在token过期时间内有效。
            //如果token本身的过期（1周），强制登录，生成新token。
            try {
                toolToken.parser(refresh);
            } catch (final Exception e) {
                System.out.println(e.getClass());
                if(e instanceof ExpiredJwtException){
                    //标记为 该更换token了呢
                    FlagToken.set(1);
                    RenewalToken.set(access);
                    //chain.doFilter(request,response);
                }else{
                    //标记为 token 无效
                    FlagToken.set(2);
                    System.out.println("无效的token");
                }
            }
            try {
                //通过令牌获取用户名称
                final String userId = toolToken.extractUserId(access);
                // todo 根据userId 从 redis ，获取用户 authentication 角色权限信息
                //判断用户不为空，且SecurityContextHolder授权信息还是空的
                final SecurityContext context = SecurityContextHolder.getContext();
                LocalUserId.set(userId);
                if (userId != null && context.getAuthentication() == null) {
                    //通过用户信息得到UserDetails
                    final UserDetails userDetails = userDetailsService.getUserById(userId);
                    //验证令牌有效性
                    final boolean validata = toolToken.validateToken(access,userId);;
                    if (validata){
                        // 将用户信息存入 authentication，方便后续校验,这个方法是要保存角色权限信息的
                        final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                        //authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));//用于限流或黑名单处理???
                        context.setAuthentication(authentication);//存放权限信息,否则会提示‘没有操作权限’
                    }
                }
            } catch (final Exception exception){
                RenewalToken.remove();
                System.out.println("你真的需要重新登录");
                FlagToken.set(3);
                /*if(exception instanceof ExpiredJwtException){
                    System.out.println("你真的需要重新登录");
                    FlagToken.set(3);
                }else{
                    FlagToken.set(2);
                }*/
            }
            //chain.doFilter(request, response);
        }
        chain.doFilter(request,response);
    }
}