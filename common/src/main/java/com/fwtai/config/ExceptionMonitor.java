package com.fwtai.config;

import com.fwtai.tool.ToolClient;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.SocketTimeoutException;

/**
 * 拦截异常并统一处理
 * @param
 * @作者 田应平
 * @QQ 444141300
 * @创建时间 2020/4/2 20:05
*/
@RestControllerAdvice
public class ExceptionMonitor{

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public void notSupported(final HttpServletResponse response){
        ToolClient.responseJson(ToolClient.exceptionJson("不支持该请求方式"),response);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public void missingServletRequest(final HttpServletResponse response){
        ToolClient.responseJson(ToolClient.exceptionJson("请选择文件再操作"),response);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public void maxUploadSize(final HttpServletResponse response){
        ToolClient.responseJson(ToolClient.exceptionJson("上传的文件过大"),response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public void accessDeniedException(final Exception exception,final HttpServletResponse response){
        System.out.println("方法accessDeniedException:"+exception.getClass());
        ToolClient.responseJson(ToolClient.notAuthorized(),response);
    }

    @ExceptionHandler({SecurityException.class})
    public void signatureException(final HttpServletResponse response){
        ToolClient.responseJson(ToolClient.createJson(199,"无效的token,请重新登录"),response);
    }

    @ExceptionHandler({ExpiredJwtException.class})
    public void expiredJwtException(final HttpServletResponse response){
        ToolClient.responseJson(ToolClient.createJson(199,"未登录或登录已过期,请重新登录"),response);
    }

    @ExceptionHandler({MalformedJwtException.class})
    public void jwtException(final HttpServletResponse response){
        ToolClient.responseJson(ToolClient.createJson(199,"登录信息已过期,请重新登录"),response);
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public void credentialsNotFoundException(final HttpServletResponse response){
        ToolClient.responseJson(ToolClient.createJson(401,"没有访问权限"),response);
    }

    @ExceptionHandler(NullPointerException.class)
    public void nullPointer(final Exception exception,final HttpServletResponse response){
        exception.printStackTrace();
        ToolClient.responseJson(ToolClient.exceptionJson("请检查是否已传入数值"),response);//注意service层的方法上是否有final关键字,否则也会这个报错,或提示系统出现错误(其实是空指针)
    }

    @ExceptionHandler(ClassCastException.class)
    public void classCast(final Exception exception,final HttpServletResponse response){
        exception.printStackTrace();
        ToolClient.responseJson(ToolClient.exceptionJson("类型转换异常"),response);
    }

    @ExceptionHandler(NumberFormatException.class)
    public void mumberFormat(final Exception exception,final HttpServletResponse response){
        exception.printStackTrace();
        ToolClient.responseJson(ToolClient.exceptionJson("数字格式异常"),response);
    }

    @ExceptionHandler({SocketTimeoutException.class,MyBatisSystemException.class,DataAccessException.class})
    public void socketTimeoutException(final HttpServletResponse response){
        ToolClient.responseJson(ToolClient.exceptionJson(),response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public void integrityViolation(final Exception exception,final HttpServletRequest request,final HttpServletResponse response){
        final String message = exception.getMessage();
        if(message.contains("Data too long for column '")){
            final int start = message.lastIndexOf("Data too long for column '") + 26;
            final int forKey = message.lastIndexOf("' at row");
            final String column = message.substring(start,forKey);
            final String value = request.getParameter(column);
            if(value != null){
                ToolClient.responseJson(ToolClient.exceptionJson("输入的[<span style='color:#f00'>"+value+ "</span>]字符过多,减少一些试试"),response);
            }else{
                ToolClient.responseJson(ToolClient.exceptionJson("输入的字符过多,减少一些试试!"),response);
            }
        }else{
            ToolClient.responseJson(ToolClient.exceptionJson("您输入字数过多"),response);
        }
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public void duplicateKeyException(final Exception exception,final HttpServletResponse response){
        final String message = exception.getMessage();
        if(message.contains("Duplicate entry '")){
            final int start = message.lastIndexOf("Duplicate entry '") + 17;
            final int forKey = message.lastIndexOf("' for key '");
            final String value = message.substring(start,forKey);
            ToolClient.responseJson(ToolClient.exceptionJson("添加编辑的[<span style='color:#f00'>"+value + "</span>]已存在"),response);
        }else{
            ToolClient.responseJson(ToolClient.exceptionJson("添加编辑的数据已存在"),response);
        }
    }

    @ExceptionHandler(Exception.class)
    public void exception(final Exception exception,final HttpServletResponse response){
        exception.printStackTrace();
        System.out.println(exception.getClass());
        ToolClient.responseJson(ToolClient.exceptionJson(),response);
    }
}