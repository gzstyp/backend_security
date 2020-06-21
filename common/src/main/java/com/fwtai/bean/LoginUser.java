package com.fwtai.bean;

import java.util.List;

public final class LoginUser{

    private String userId;

    /** 角色列表*/
    private List<String> roleSet;

    /**权限列表*/
    private List<String> permissionsSet;

    public String getUserId(){
        return userId;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public List<String> getRoleSet(){
        return roleSet;
    }

    public void setRoleSet(List<String> roleSet){
        this.roleSet = roleSet;
    }

    public List<String> getPermissionsSet(){
        return permissionsSet;
    }

    public void setPermissionsSet(List<String> permissionsSet){
        this.permissionsSet = permissionsSet;
    }
}