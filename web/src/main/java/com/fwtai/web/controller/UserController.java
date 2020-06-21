package com.fwtai.web.controller;

import com.fwtai.bean.PageFormData;
import com.fwtai.service.web.UserService;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolJWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * 用户管理接口入口
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-03-13 17:14
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
*/
@RestController
@RequestMapping("/user")
public class UserController{

    @Resource
    private ToolJWT toolJWT;

    @Resource
    private UserService userService;

    @PreAuthorize("hasAuthority('user:btn:listData')")
    @GetMapping(value = "/permissions")
    public void permissions(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(userService.permissions(new PageFormData(request)),response);
    }

    @PostMapping(value = "/renewalToken")
    public void renewalToken(final HttpServletRequest request,final HttpServletResponse response){
        final PageFormData formData = ToolClient.getFormData(request);
        final String access_token = formData.getString("access_token");
        try {
            final Claims claims = toolJWT.parser(access_token);
            final String userId = claims.getId();
            final List<String> permissionRole = claims.get(userId,List.class);
            final HashMap<String,String> result = userService.buildToken(userId);
            System.err.println("-----------已成功更新令牌并返回-----------");
            ToolClient.responseJson(ToolClient.queryJson(result),response);
        } catch (final JwtException exception){
            System.out.println("你真的需要重新登录");
            ToolClient.responseJson(ToolClient.tokenInvalid(),response);
        }
    }

    /**添加*/
    @PreAuthorize("hasAuthority('user:btn:add')")
    @PostMapping(value = "/add",name = "user:btn:add")
    public void add(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(userService.add(ToolClient.getFormData(request)),response);
    }

    /**编辑*/
    @PreAuthorize("hasAuthority('user:row:edit')")
    @PostMapping(value = "/edit",name = "user:row:edit")
    public void edit(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(userService.edit(ToolClient.getFormData(request)),response);
    }

    /**删除-单行*/
    @PreAuthorize("hasAuthority('user:row:delById')")
    @PostMapping(value = "/delById",name = "user:row:delById")
    public void delById(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(userService.delById(ToolClient.getFormData(request)),response);
    }

    /**批量删除*/
    @PreAuthorize("hasAuthority('user:btn:delByKeys')")
    @PostMapping(value = "/delByKeys",name = "user:btn:delByKeys")
    public void delByKeys(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(userService.delByKeys(ToolClient.getFormData(request)),response);
    }

    /**获取数据*/
    @PreAuthorize("hasAuthority('user:btn:listData')")
    @GetMapping(value = "/listData",name = "user:btn:listData")
    public void listData(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(userService.listData(ToolClient.getFormData(request)),response);
    }

    /**获取角色数据*/
    @PreAuthorize("hasAuthority('user:btn_row:getAllotRole')")
    @GetMapping(value = "/getAllotRole",name = "user:btn_row:getAllotRole")
    public void getAllotRole(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(userService.getAllotRole(ToolClient.getFormData(request)),response);
    }

    /**保存分配角色*/
    @PreAuthorize("hasAuthority('user:btn_row:saveAllotRole')")
    @PostMapping(value = "/saveAllotRole",name = "user:btn_row:saveAllotRole")
    public void saveAllotRole(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(userService.saveAllotRole(ToolClient.getFormData(request)),response);
    }

    /**控制启禁用*/
    @PreAuthorize("hasAuthority('user:row:editEnabled')")
    @PostMapping(value = "/editEnabled",name = "user:row:editEnabled")
    public void editEnabled(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(userService.editEnabled(ToolClient.getFormData(request)),response);
    }

    /**根据指定userid获取菜单用于分配私有菜单*/
    @PreAuthorize("hasAuthority('user:row:getOwnMenu')")
    @GetMapping(value = "/getOwnMenu",name = "user:row:getOwnMenu")
    public void getOwnMenu(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(userService.getOwnMenu(ToolClient.getFormData(request)).replaceAll("\"false\"","false").replaceAll("\"true\"","true"),response);
    }

    /**保存私有菜单(用户菜单)*/
    @PreAuthorize("hasAuthority('user:row:saveOwnMenu')")
    @PostMapping(value = "/saveOwnMenu",name = "user:row:saveOwnMenu")
    public void saveOwnMenu(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(userService.saveOwnMenu(ToolClient.getFormData(request)),response);
    }

    /**查看指定userid权限菜单数据*/
    @PreAuthorize("hasAuthority('user:row:getMenuData')")
    @GetMapping(value = "/getMenuData",name = "user:row:getMenuData")
    public void getMenuData(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(userService.getMenuData(ToolClient.getFormData(request)).replaceAll("\"false\"","false").replaceAll("\"true\"","true"),response);
    }

    @GetMapping(value = "/notAuthorized",name = "user:notAuthorized")
    public void notAuthorized(final HttpServletResponse response){
        ToolClient.responseJson(ToolClient.notAuthorized(),response);
    }
}