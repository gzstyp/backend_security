package com.fwtai.dao;

import com.fwtai.bean.PageFormData;
import com.fwtai.datasource.DaoHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 角色管理
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020/4/9 13:43
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
@Repository
public class RoleDao{

    @Autowired
    private DaoHandle dao;

    /**添加或编辑时查询角色名称是否已存在,查询得到的kid是否存在或和本次修改的id是否一致来判断是否存在*/
    public String queryRoleName(final String roleName){
        return dao.queryForString("sys_role.queryRoleName",roleName);
    }

    /**添加或编辑时查询角色标识是否已存在,查询得到的kid是否存在或和本次修改的id是否一致来判断是否存在*/
    public String queryRoleFlag(final String roleFlag){
        return dao.queryForString("sys_role.queryRoleFlag",roleFlag);
    }

    public String queryExistById(final String kid){
        return dao.queryForString("sys_role.queryExistById",kid);
    }

    public int add(final PageFormData pageFormData){
        return dao.execute("sys_role.add",pageFormData);
    }

    public int edit(final PageFormData pageFormData){
        return dao.execute("sys_role.edit",pageFormData);
    }

    public int delById(final String kid){
        return dao.execute("sys_role.del",kid);
    }

    @Transactional
    public int delByKeys(final ArrayList<String> list){
        dao.execute("sys_role.delBatchRoleMenu",list);
        return dao.execute("sys_role.delBatchRoles",list);
    }

    public HashMap<String,Object> listData(final PageFormData pageFormData){
        return dao.queryForPage(pageFormData,"sys_role.listData","sys_role.listTotal");
    }

    public int delEmptyMenu(final String roleId){
        return dao.execute("sys_role.delEmptyMenu",roleId);
    }

    public List<HashMap<String,String>> getRoleMenu(final String roleId){
        return dao.queryForListString("sys_role.getRoleMenu",roleId);
    }

    //清空之前的菜单再保存新的菜单菜单
    @Transactional
    public int saveRoleMenu(final String roleId,final ArrayList<HashMap<String,String>> lists){
        dao.execute("sys_role.delEmptyMenu",roleId);
        return dao.execute("sys_role.saveRoleMenu",lists);
    }
}