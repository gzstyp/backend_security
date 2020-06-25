package com.fwtai.tool;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 防暴力破解
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-06-25 16:54
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
*/
@Component
public class ToolAttack{

    private final String lock = "lock";

    private final Integer MAX_ATTACK = 5 ;//5次登录失败则记录ip,注意nginx代理时的处理ip

    private LoadingCache<String,Integer> attackCache;

    private LoadingCache<String,String> lockTime;

    public ToolAttack(){
        this.attackCache = CacheBuilder.newBuilder().expireAfterWrite(30,TimeUnit.MINUTES).build(new CacheLoader<String,Integer>(){
            @Override
            public Integer load(final String key) throws Exception{
                return 0;
            }
        });
        this.lockTime = CacheBuilder.newBuilder().expireAfterWrite(30,TimeUnit.MINUTES).build(new CacheLoader<String,String>(){
            @Override
            public String load(final String key) throws Exception{
                return "";
            }
        });
    }

    /*登录成功后移除ip*/
    public void loginSucceed(final String ip){
        this.attackCache.invalidate(ip);
    }

    /*如果登录失败计数器 +1*/
    public void loginFailed(final String ip){
        Integer attempts = getValue(ip);
        attempts++;
        attackCache.put(ip,attempts);
    }

    /*是否被屏蔽*/
    public boolean isBlocked(final String ip){
        return getValue(ip) >= MAX_ATTACK;
    }

    /*获取登录IP错误次数*/
    public Integer getCount(final String ip){
        return getValue(ip);
    }

    private Integer getValue(final String ip){
        try{
            return attackCache.get(ip);
        }catch(final ExecutionException e){
            e.printStackTrace();
        }
        return 0;
    }

    public void setLockTime(final String ip){
        final String key = lock+ip;
        final String value = ZonedDateTime.now(ZoneId.of("Asia/Shanghai")).plusMinutes(30).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        lockTime.put(key,value);
    }

    public String getLockTime(final String ip){
        final String key = lock+ip;
        try{
            return lockTime.get(key);
        }catch(final ExecutionException e){}
        return "";
    }

    public void removeLockTime(final String ip){
        final String key = lock+ip;
        this.lockTime.invalidate(key);
    }
}












































