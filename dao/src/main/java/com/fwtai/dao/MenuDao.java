package com.fwtai.dao;

import com.fwtai.bean.Menu;
import com.fwtai.bean.PageFormData;
import com.fwtai.datasource.DaoHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜单维护管理
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-04-08 23:30
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
@Repository
public class MenuDao{

    @Autowired
    private DaoHandle dao;

    /**添加或编辑时查询权限是否已存在,根据permission去查询得到的kid再和本次修改的id是否一致来判断是否存在*/
    public String queryPermissionExist(final String permission){
        return dao.queryForString("sys_menu.queryPermissionExist",permission);
    }

    /**添加或编辑时查询url是否已存在,根据url去查询得到的kid再和本次修改的id是否一致来判断是否存在*/
    public String queryUrlExist(final String url){
        return dao.queryForString("sys_menu.queryUrlExist",url);
    }

    public String queryRelation(final String pid){
        return dao.queryForString("sys_menu.getRelation",pid);
    }

    public String queryPidExistChid(final String pid){
        return dao.queryForString("sys_menu.queryPidExistChid",pid);
    }

    public int add(final PageFormData pageFormData){
        return dao.execute("sys_menu.add",pageFormData);
    }

    public int edit(final PageFormData pageFormData){
        return dao.execute("sys_menu.edit",pageFormData);
    }

    /**删除-单行*/
    @Transactional
    public int delById(final String kid){
        final int rows = dao.execute("sys_menu.delById",kid);
        dao.execute("sys_menu.del_menu_account",kid);//删除与该id相关联的账号私有菜单表
        dao.execute("sys_menu.del_menu_role",kid);//删除与该id相关联的角色菜单表
        return rows;
    }

    public String queryKeyId(final String kid){
        return dao.queryForString("sys_menu.queryUseTotal",kid);
    }

    public List<Menu> getMenuData(final String userId){
        return dao.queryListEntity("sys_menu.getMenus",userId);
    }

    public HashMap<String,Object> listData(final PageFormData pageFormData){
        return dao.queryForPage(pageFormData,"sys_menu.listData","sys_menu.listTotal");
    }

    public HashMap<String, Object> queryById(final String kid){
        return dao.queryForHashMap("sys_menu.queryById",kid);
    }

    public List<Map<String, Object>> queryAllMenu(final String kid){
        return dao.queryForListMap("sys_menu.queryAllMenu",kid);
    }
}