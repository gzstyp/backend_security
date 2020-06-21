package com.fwtai.bean;

public class SysUser{

    private String kid;

    private String userName;

    private String userPassword;

    private Integer enabled;

    private Integer errorCount;

    private String errorTime;

    public String getKid(){
        return kid;
    }

    public void setKid(String kid){
        this.kid = kid;
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getUserPassword(){
        return userPassword;
    }

    public void setUserPassword(String userPassword){
        this.userPassword = userPassword;
    }

    public Integer getEnabled(){
        return enabled;
    }

    public void setEnabled(Integer enabled){
        this.enabled = enabled;
    }

    public Integer getErrorCount(){
        return errorCount;
    }

    public void setErrorCount(Integer errorCount){
        this.errorCount = errorCount;
    }

    public String getErrorTime(){
        return errorTime;
    }

    public void setErrorTime(String errorTime){
        this.errorTime = errorTime;
    }
}