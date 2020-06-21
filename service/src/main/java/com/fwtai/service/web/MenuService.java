package com.fwtai.service.web;

import com.fwtai.bean.Menu;
import com.fwtai.bean.PageFormData;
import com.fwtai.config.ConfigFile;
import com.fwtai.dao.MenuDao;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolMenuBean;
import com.fwtai.tool.ToolMenuEntity;
import com.fwtai.tool.ToolString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * 菜单管理
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-02-20 15:31
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
@Service
public class MenuService{

    @Autowired
    private MenuDao menuDao;

    public String add(final PageFormData pageFormData){
        final String p_name = "name";
        final String p_url = "url";
        final String p_permission = "permission";
        final String p_category = "category";
        final String p_pid = "pid";
        final String p_type = "type";
        final String p_subset = "subset";
        final String validate = ToolClient.validateField(pageFormData,new String[]{p_name,p_permission,p_url,p_category,p_type,p_subset});
        if(validate != null)return validate;
        final String fieldInteger = ToolClient.validateInteger(pageFormData,new String[]{"category","subset","type"});
        if(fieldInteger != null)return fieldInteger;
        final String order_by = pageFormData.getString("order_by");
        if(order_by != null){
            try {
                Integer.parseInt(order_by);
            } catch (Exception e) {
                return ToolClient.createJsonFail("显示排序只能是数字");
            }
        }
        final String url = pageFormData.getString(p_url);
        final String exist_key_url = menuDao.queryUrlExist(url);
        if(exist_key_url != null){
            return ToolClient.createJson(ConfigFile.code199,"菜单url路径<br/><span style='color:#f00'>"+url+"</span><br/>已存在,请换一个");
        }
        final String permission = pageFormData.getString(p_permission);
        final String exist_key_permission = menuDao.queryPermissionExist(permission);
        if(exist_key_permission != null){
            return ToolClient.createJson(ConfigFile.code199,"菜单权限标识<br/><span style='color:#f00'>"+permission+"</span><br/>已存在,请换一个");
        }
        final String pid = pageFormData.getString(p_pid);
        final int type = pageFormData.getInteger(p_type);
        if(type != 1){
            if(pid == null){
                return ToolClient.createJsonFail("菜单类型和父节点有误");
            }
        }
        final String kid = ToolString.getIdsChar32();
        if(pid == null){
            if(type == 2 || type == 3){
                return ToolClient.createJsonFail("菜单类型和父节点有误");
            }
            final String icon_style = pageFormData.getString("icon_style");
            if(icon_style == null){
                return ToolClient.createJsonFail("请输入菜单图标");
            }
            if(!icon_style.contains("menu-icon")){
                pageFormData.put("icon_style","menu-icon "+icon_style);
            };
            pageFormData.put("pid",ConfigFile.pid);
            pageFormData.put("relation",kid);
        }else{
            pageFormData.remove("icon_style");
            final String relation = menuDao.queryRelation(pid);
            if(relation.length() >= 258){
                if(type == 1){
                    return ToolClient.createJsonFail("导航菜单的层级不能多于8级");
                }
            }
            pageFormData.put("relation",relation+"@"+kid);//查询父节点的relation
        }
        pageFormData.put("kid",kid);
        final int rows = menuDao.add(pageFormData);
        return ToolClient.executeRows(rows);
    }

    public String edit(final PageFormData pageFormData){
        final String p_name = "name";
        final String p_url = "url";
        final String p_permission = "permission";
        final String p_category = "category";
        final String p_kid = "kid";
        final String p_pid = "pid";
        final String p_type = "type";
        final String p_subset = "subset";
        final String validate = ToolClient.validateField(pageFormData,new String[]{p_name,p_permission,p_url,p_category,p_type,p_subset,p_kid,p_pid});
        if(validate != null)return validate;
        final String fieldInteger = ToolClient.validateInteger(pageFormData,new String[]{"category","subset","type"});
        if(fieldInteger != null)return fieldInteger;
        final String order_by = pageFormData.getString("order_by");
        if(order_by != null){
            try {
                Integer.parseInt(order_by);
            } catch (Exception e) {
                return ToolClient.createJsonFail("显示排序只能是数字");
            }
        }
        final String kid = (String)pageFormData.get(p_kid);
        final String url = pageFormData.getString(p_url);
        final String exist_key_url = menuDao.queryUrlExist(url);
        if(exist_key_url != null){
            if(!exist_key_url.equals(kid)){
                return ToolClient.createJson(ConfigFile.code199,"菜单url路径<br/><span style='color:#f00'>"+url+"</span><br/>已存在,请换一个");
            }
        }
        final String permission = pageFormData.getString(p_permission);
        final String exist_key_permission = menuDao.queryPermissionExist(permission);
        if(exist_key_permission != null){
            if(!exist_key_permission.equals(kid)){
                return ToolClient.createJson(ConfigFile.code199,"菜单权限标识<br/><span style='color:#f00'>"+permission+"</span><br/>已存在,请换一个");
            }
        }
        final String pid = pageFormData.getString(p_pid);
        final int type = pageFormData.getInteger(p_type);
        if(type != 1){
            if(pid == null){
                return ToolClient.createJsonFail("菜单类型和父节点有误");
            }
            //查询是否还有以普通按钮或行内按钮作为父级菜单
            final String existChid = menuDao.queryPidExistChid(pid);
            if(existChid != null){
                final String msg = type == 2 ? "普通按钮" : "行内按钮";
                return ToolClient.createJsonFail(msg+"不能是导航菜单");
            }
            pageFormData.remove("icon_style");
        }
        if(pid.equals(ConfigFile.pid)){
            final String icon_style = pageFormData.getString("icon_style");
            if(icon_style == null){
                return ToolClient.createJsonFail("请输入菜单图标");
            }
            if(!icon_style.contains("menu-icon")){
                pageFormData.put("icon_style","menu-icon "+icon_style);
            }
        }else{
            final String relation = menuDao.queryRelation(pid);
            if(relation.length() >= 258){
                if(type == 1){
                    return ToolClient.createJsonFail("导航菜单的层级不能多于8级");
                }
            }
            pageFormData.put("relation",relation+"@"+kid);//查询父节点的relation
        }
        final int rows = menuDao.edit(pageFormData);
        return ToolClient.executeRows(rows);
    }

    public String delById(final PageFormData pageFormData){
        final String p_kid = "id";
        final String validate = ToolClient.validateField(pageFormData,p_kid);
        if(validate != null)return validate;
        final String kid = pageFormData.getString(p_kid);
        final String exist_key_kid = menuDao.queryKeyId(kid);
        if(exist_key_kid != null){
            return ToolClient.createJson(ConfigFile.code199,"所删除的菜单有子页面|普通按钮|行内按钮,请先删除它们再操作");
        }
        final int rows = menuDao.delById(kid);
        return ToolClient.executeRows(rows);
    }

    //获取当前登录人的所拥有的菜单
    public String getMenuData(final String userId){
        try {
            final List<Menu> lists = menuDao.getMenuData(userId);
            if(lists.size() <= 0) return null;
            final List<Menu> rootMenu = new ToolMenuEntity().initMenu(lists,ConfigFile.pid);//需要指定最外层的父级id
            return new ToolMenuBean().meunForHtml(rootMenu);
        } catch (Exception e){
            return null;
        }
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
            final HashMap<String,Object> map = menuDao.listData(pageFormData);
            return ToolClient.dataTableOK((List<Object>)map.get(ConfigFile.rows),map.get(ConfigFile.total),pageFormData.get("sEcho"));
        } catch (Exception e){
            e.printStackTrace();
            return ToolClient.dataTableException(pageFormData.get("sEcho"));
        }
    }

    public String queryById(final PageFormData pageFormData){
        final String p_id = "id";
        final String validate = ToolClient.validateField(pageFormData,p_id);
        if(validate != null)return validate;
        final String kid = pageFormData.getString(p_id);
        return ToolClient.queryJson(menuDao.queryById(kid));
    }

    public String queryTreeMenu(final PageFormData pageFormData){
        final String id = pageFormData.getString("kid");
        final String kid = id == null ? ConfigFile.pid : id;
        return ToolClient.queryJson(menuDao.queryAllMenu(kid));
    }
}