package com.fwtai.api.controller;

import com.fwtai.tool.ToolClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * app端用户中心
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

    /*Post请求 保存用户信息*/
    @ApiOperation(value = "post请求 保存用户信息", notes = "输入录用户（手机号码）和密码")
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
    }

    //放行对外提供注册接口url
    @PostMapping(value = "/register")
    public void register(final HttpServletResponse response){
        ToolClient.responseJson(ToolClient.createJsonSuccess("post的register,无token"),response);
    }
}