package com.fwtai.dao;

import com.fwtai.datasource.DaoHandle;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * 异步处理
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-04-10 20:13
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
@Repository
public class AsyncDao{

    @Resource
    private DaoHandle dao;

    public void updateSucceed(final String userId){
        dao.execute("sys_user.updatelogintime",userId);/*最后登录时间*/
        dao.execute("sys_user.updateTimes",userId);/*更新登录次数*/
        dao.execute("sys_user.updateErrorTime",userId);/*登录成功把时间设置为当前默认时间*/
        dao.execute("sys_user.updateErrorCount",userId);/*登录成功把登录错误次数更改为0*/
    }
}