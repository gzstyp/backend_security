package com.fwtai.auth;

import com.fwtai.config.ConfigFile;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 解决跨域问题
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020/4/29 20:42
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer{

    /**
     * 授权拦截的路径 addPathPatterns：拦截的路径 excludePathPatterns：不拦截的路径
     */
    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor()).addPathPatterns("/**").excludePathPatterns(ConfigFile.IGNORE_URLS);
    }

    @Override
    public void addCorsMappings(final CorsRegistry registry){
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("PUT","DELETE","GET","POST","OPTIONS")
            .allowedHeaders("*")
            .exposedHeaders("access-control-allow-headers","access-control-allow-methods","access-control-allow-origin","access-control-max-age","X-Frame-Options")
            .allowCredentials(true)
            .maxAge(7200l);
    }
}
