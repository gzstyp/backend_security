package com.fwtai.auth;

import com.fwtai.config.ConfigFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 退出Handler
 */
@Component
public class LogoutService implements LogoutHandler {

    @Override
    public void logout(final HttpServletRequest request,final HttpServletResponse response,final Authentication authentication) {
        final String token = request.getHeader(ConfigFile.ACCESS_TOKEN);
        if (!StringUtils.isEmpty(token)) {
            System.out.println("accessToken = " + authentication);
            SecurityContextHolder.clearContext();
        }
    }
}