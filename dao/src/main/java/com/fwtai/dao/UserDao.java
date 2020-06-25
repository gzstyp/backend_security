package com.fwtai.dao;

import com.fwtai.bean.PageFormData;
import com.fwtai.bean.SysUser;
import com.fwtai.datasource.DaoHandle;
import com.fwtai.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 用户账号管理
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020/4/9 13:43
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
@Repository
public class UserDao{

    @Autowired
    private DaoHandle dao;

    @Transactional
    public int add(final PageFormData pageFormData){
        final int row = dao.execute("sys_user.addUser",pageFormData);
        dao.execute("sys_user.addPassword",pageFormData);
        return row;
    }

    public int edit(final PageFormData pageFormData){
        return dao.execute("sys_user.editPassword",pageFormData);
    }

    public String queryExistById(final String kid){
        return dao.queryForString("sys_user.queryExistById",kid);
    }

    public String queryExistByUser(final String userName){
        return dao.queryForString("sys_user.queryExistByUser",userName);
    }

    @Transactional
    public int delById(final String kid){
        final int row = dao.execute("sys_user.delUser",kid);
        dao.execute("sys_user.delPassword",kid);
        dao.execute("sys_user.delUserMenu",kid);
        dao.execute("sys_user.delUserRole",kid);
        return row;
    }

    @Transactional
    public int saveAllotRole(final String userId,final ArrayList<HashMap<String,String>> lists){
        //删除之前的角色,重新分配角色
        dao.execute("sys_user.delRoleUser",userId);
        return dao.execute("sys_user.addRoleForUser",lists);
    }

    public int delAllotRole(final String userId){
        return dao.execute("sys_user.delRoleUser",userId);
    }

    //批量删除角色
    public int delBatchUserRole(final ArrayList<String> list){
        return dao.execute("sys_user.delListUserRole",list);
    }

    //清空私有菜单
    public int delOwnMenu(final String kid){
        return dao.execute("sys_user.delUserMenu",kid);
    }

    //清空私有菜单再保存私有菜单
    @Transactional
    public int saveOwnMenu(final String kid,final ArrayList<HashMap<String,String>> lists){
        dao.execute("sys_user.delUserMenu",kid);
        return dao.execute("sys_user.saveOwnMenu",lists);
    }

    //删除之前的角色,重新分配角色
    @Transactional
    public int saveBatchUserRole(final ArrayList<String> listUserIds,final ArrayList<HashMap<String,String>> lists){
        dao.execute("sys_user.delListUserRole",listUserIds);
        return dao.execute("sys_user.saveRoleIds",lists);
    }

    @Transactional
    public int delByKeys(final ArrayList<String> list){
        final int row = dao.execute("sys_user.delListUser",list);
        dao.execute("sys_user.delListPassword",list);
        dao.execute("sys_user.delListUserMenu",list);
        dao.execute("sys_user.delListUserRole",list);
        return row;
    }

    public int editEnabled(final PageFormData pageFormData){
        return dao.execute("sys_user.editEnabled",pageFormData);
    }

    public List<HashMap<String,String>> getAllotRole(final String userId){
        if(userId != null){
            return dao.queryForListString("sys_user.getUserIdAllotRole",userId);
        }else{
            return dao.queryForListString("sys_user.getAllotRole");
        }
    }

    public HashMap<String,Object> listData(final PageFormData pageFormData){
        return dao.queryForPage(pageFormData,"sys_user.listData","sys_user.listTotal");
    }

    public String queryLogin(final String username){
        return dao.queryForString("sys_user.login",username);
    }

    // 仅仅获取用户userId的角色和权限
    public List<String> getRolePermissions(final String userId){
        return dao.queryListEntity("sys_user.getRolePermissions",userId);
    }

    /**
     * 通过userName查询用户信息,用户登录
     * @param username
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2020/5/1 0:54
     */
    public SysUser getUserByUserName(final String username){
        return dao.queryForEntity("sys_user.getUserByUserName",username);
    }

    /**
     * 通过userId查询用户的全部角色和权限的信息
     * @param userId
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2020/5/1 0:53
     */
    public SysUser getUserById(final String userId){
        return dao.queryForEntity("sys_user.getUserById",userId);
    }

    public User queryUser(final String userName){
        return dao.queryForEntity("sys_user.queryUser",userName);
    }

    public void updateErrors(final String userName){
        dao.execute("sys_user.updateErrors",userName);
    }

    public void updateLoginTime(final String userName){
        dao.execute("sys_user.updateLoginTime",userName);
    }

    public List<HashMap<String,String>> getOwnMenu(final String userId){
        return dao.queryForListString("sys_user.getOwnMenu",userId);
    }

    public List<HashMap<String,String>> getMenuData(final String userId){
        return dao.queryForListString("sys_user.getMenuData",userId);
    }

    public List<String> permissions(final HashMap<String,String> params){
        return dao.queryListString("sys_user.permissions",params);
    }
}