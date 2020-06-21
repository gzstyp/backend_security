package com.fwtai.service.web;

import com.fwtai.bean.PageFormData;
import com.fwtai.config.ConfigFile;
import com.fwtai.dao.RoleDao;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolString;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * 角色管理
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-03-13 17:24
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
*/
@Service
public class RoleService{

    @Resource
    private RoleDao roleDao;

    public String add(final PageFormData pageFormData){
        final String p_role_name = "role_name";
        final String p_role_flag = "role_flag";
        final String validate = ToolClient.validateField(pageFormData,p_role_name,p_role_flag);
        if(validate != null)return validate;
        final String role_name = pageFormData.getString(p_role_name);
        final String role_flag = pageFormData.getString(p_role_flag);
        final String exist_role_name = roleDao.queryRoleName(role_name);
        if(exist_role_name != null){
            return ToolClient.createJson(ConfigFile.code199,"角色名称<br/><span style='color:#f00'>"+role_name+"</span><br/>已存在,请换一个");
        }
        final String exist_role_flag = roleDao.queryRoleFlag(role_flag);
        if(exist_role_flag != null){
            return ToolClient.createJson(ConfigFile.code199,"角色标识<br/><span style='color:#f00'>"+role_flag+"</span><br/>已存在,请换一个");
        }
        pageFormData.put("kid",ToolString.getIdsChar32());
        return ToolClient.executeRows(roleDao.add(pageFormData));
    }

    public String edit(final PageFormData pageFormData){
        final String p_role_name = "role_name";
        final String p_role_flag = "role_flag";
        final String p_kid = "kid";
        final String validate = ToolClient.validateField(pageFormData,p_role_name,p_role_flag,p_kid);
        if(validate != null)return validate;
        final String role_name = pageFormData.getString(p_role_name);
        final String role_flag = pageFormData.getString(p_role_flag);
        final String kid = pageFormData.getString(p_kid);
        final String exist_role_name = roleDao.queryRoleName(role_name);
        if(exist_role_name != null){
            if(!exist_role_name.equals(kid)){
                return ToolClient.createJson(ConfigFile.code199,"角色名称<br/><span style='color:#f00'>"+role_name+"</span><br/>已存在,请换一个");
            }
        }
        final String exist_role_flag = roleDao.queryRoleFlag(role_flag);
        if(exist_role_flag != null){
            if(!exist_role_flag.equals(kid)){
                return ToolClient.createJson(ConfigFile.code199,"角色标识<br/><span style='color:#f00'>"+role_flag+"</span><br/>已存在,请换一个");
            }
        }
        return ToolClient.executeRows(roleDao.edit(pageFormData));
    }

    public String delById(final PageFormData pageFormData){
        final String p_kid = "id";
        final String validate = ToolClient.validateField(pageFormData,p_kid);
        if(validate != null)return validate;
        final String kid = pageFormData.getString(p_kid);
        final String exist_key_kid = roleDao.queryExistById(kid);
        if(exist_key_kid == null){
            return ToolClient.createJson(ConfigFile.code199,"角色已不存在");
        }
        return ToolClient.executeRows(roleDao.delById(kid));
    }

    public String delByKeys(final PageFormData pageFormData){
        final String p_ids = "ids";
        final String validate = ToolClient.validateField(pageFormData,p_ids);
        if(validate != null)return validate;
        final String ids = pageFormData.getString(p_ids);
        final ArrayList<String> lists = ToolString.keysToList(ids);
        return ToolClient.executeRows(roleDao.delByKeys(lists),"操作成功","角色已不存在,刷新重试");
    }

    public String listData(PageFormData pageFormData){
        final String p_iColumns = "iColumns";
        final String validate = ToolClient.validateField(pageFormData,p_iColumns);
        if(validate != null)return validate;
        final String fieldInteger = ToolClient.validateInteger(pageFormData,p_iColumns);
        if(fieldInteger != null)return fieldInteger;
        try {
            pageFormData = ToolClient.dataTableMysql(pageFormData);
            if(pageFormData == null)return ToolClient.jsonValidateField();
            final HashMap<String,Object> map = roleDao.listData(pageFormData);
            return ToolClient.dataTableOK((List<Object>)map.get(ConfigFile.rows),map.get(ConfigFile.total),pageFormData.get("sEcho"));
        } catch (Exception e){
            e.printStackTrace();
            return ToolClient.dataTableException(pageFormData.get("sEcho"));
        }
    }

    public String delEmptyMenu(final PageFormData pageFormData){
        final String p_kid = "kid";
        final String validate = ToolClient.validateField(pageFormData,p_kid);
        if(validate != null)return validate;
        final String roleId = pageFormData.getString(p_kid);
        return ToolClient.executeRows(roleDao.delEmptyMenu(roleId));
    }


    //根据指定roleId获取角色菜单
    public String getRoleMenu(final PageFormData pageFormData){
        final String p_roleId = "roleId";
        final String validate = ToolClient.validateField(pageFormData,p_roleId);
        if(validate != null)return validate;
        final String roleId = pageFormData.getString(p_roleId);
        return ToolClient.queryJson(roleDao.getRoleMenu(roleId));
    }

    public String saveRoleMenu(final PageFormData pageFormData){
        final String p_roleId = "roleId";
        final String validate = ToolClient.validateField(pageFormData,p_roleId);
        if(validate != null)return validate;
        final String kids = pageFormData.getString("kids");
        final String roleId = pageFormData.getString(p_roleId);
        final ArrayList<String> listMenus = ToolString.keysToList(kids);
        if(listMenus == null || listMenus.size() <= 0){
            final int rows = roleDao.delEmptyMenu(roleId);
            return ToolClient.executeRows(rows,"操作成功","未做任何操作,因为暂无私有菜单");
        }else{
            final ArrayList<HashMap<String,String>> listMaps = new ArrayList<HashMap<String,String>>();
            final Iterator<String> iterator = listMenus.iterator();
            while(iterator.hasNext()){
                final HashMap<String,String> map = new HashMap<String,String>();
                map.put("kid",ToolString.getIdsChar32());
                map.put("menu_id",iterator.next());
                map.put("role_id",roleId);
                listMaps.add(map);
            }
            final int rows = roleDao.saveRoleMenu(roleId,listMaps);
            return ToolClient.executeRows(rows);
        }
    }


















}