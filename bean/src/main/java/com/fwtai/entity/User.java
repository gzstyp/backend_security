package com.fwtai.entity;

import java.io.Serializable;

public class User implements Serializable {

    private String kid;

    private String userName;

    private Integer enabled;

    private Integer errorCount;

    private Long error;

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

    public Long getError(){
        return error;
    }

    public void setError(Long error){
        this.error = error;
    }

    public String getErrorTime(){
        return errorTime;
    }

    public void setErrorTime(String errorTime){
        this.errorTime = errorTime;
    }
}