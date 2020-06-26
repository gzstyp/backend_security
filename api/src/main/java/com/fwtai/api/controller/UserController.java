package com.fwtai.api.controller;

import com.fwtai.bean.PageFormData;
import com.fwtai.service.web.UserService;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolJWT;
import io.jsonwebtoken.JwtException;
import io.swagger.annotations.Api;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * app端用户中心,list或查询详细信息时，不需要token的,而添加、编辑、删除时需要登录
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-03-24 12:36
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
@Api(tags = "app端用户中心")
@RestController
@RequestMapping("/api/v1.0/user")
public class UserController{

    @Resource
    private ToolJWT toolJWT;

    @Resource
    private UserService userService;

    /*更新token*/
    @PostMapping("/renewalToken")
    public void renewalToken(final HttpServletRequest request,final HttpServletResponse response){
        final PageFormData formData = ToolClient.getFormData(request);
        final String access_token = formData.getString("access_token");
        try {
            final String userId = toolJWT.extractUserId(access_token);
            final HashMap<String,String> result = userService.buildToken(userId);
            ToolClient.responseJson(ToolClient.queryJson(result),response);
        } catch (final JwtException exception){
            ToolClient.responseJson(ToolClient.tokenInvalid(),response);
        }
    }

    /*Post请求 保存用户信息*/
   /*@ApiOperation(value = "post请求 保存用户信息", notes = "输入录用户（手机号码）和密码")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "userName", value = "登录登录用户（手机号码）", dataType = "String", paramType = "query", required = true),
    })
    @PostMapping(value = "/save")
    public String save(final HttpServletRequest request) {
        return "保存用户："+request.getParameter("userName")+"操作成功！";
    }

    @ApiOperation(value = "app端登录", notes = "app端登录,输入录用户（手机号码）和密码")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "phone", value = "登录登录用户（手机号码）", dataType = "String", paramType = "query", required = true),
        @ApiImplicitParam(name = "password", value = "登录密码", dataType = "String", paramType = "query", required = true),
        @ApiImplicitParam(name = "type", value = "登录类型1 为android ; 2 ios;", dataType = "int", example = "1",paramType = "query", required = true),
    })
    @PostMapping("login")
    public void login(final HttpServletResponse response){
        ToolClient.responseJson(ToolClient.createJsonSuccess("操作成功"),response);
    }*/

    //放行对外提供注册接口url
    @PostMapping(value = "/register")
    public void register(final HttpServletResponse response){
        ToolClient.responseJson(ToolClient.createJsonSuccess("不携带token使用post访问register成功"),response);
    }

    /**编辑*/
    @PreAuthorize("hasAuthority('role:row:edit')")
    @PostMapping("/edit")
    public void edit(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(ToolClient.createJsonSuccess("需要token才能访问edit成功"),response);
    }
}