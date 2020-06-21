package com.fwtai.tool;

import com.fwtai.bean.Menu;
import com.fwtai.config.ConfigFile;

import java.util.List;

/**
 * 通过list树形菜单拼接无限级菜单
 * @用法
 * final List<Menu> lists = dao.selectListEntity("sys_core_menu.getBeanMenus");
 * final List<Menu> rootMenu = new ToolMenuEntity().initMenu(lists,ConfigFile.pid);//需要指定最外层的父级id,这里是32个8,死值
 * return new ToolMenuBean().meunForHtml(rootMenu);
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2018-06-02 10:02
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
 * @参考 <uri>https://blog.csdn.net/oqqMuSe/article/details/79413203</uri>
*/
public final class ToolMenuBean{

    public final String meunForHtml(final List<Menu> list){
        final StringBuilder sb = new StringBuilder();
        for(int i = 0; i < list.size(); i++){
            final Menu menu = list.get(i);
            sb.append("<li class=\"\">");
            final Integer subset = menu.getSubset();
            if(subset == 1){
                sb.append("<a href=\"javascript:;\" class=\"dropdown-toggle\">");
            }else{
                sb.append("<a data-url=\""+ menu.getUrl()+"\" href=\"#"+ menu.getUrl()+"\" >");
            }
            sb.append("<i class=\""+menu.getIconStyle()+"\"></i>");
            sb.append("<span class=\"menu-text\">"+ menu.getName()+"</span>");
            final int size = menu.getChildren().size();
            if(size > 0){
                sb.append("<b class=\"arrow fa fa-angle-down\"></b>");
            }
            sb.append("</a>");
            sb.append("<b class=\"arrow\"></b>");
            if(size > 0){
                sb.append(MenuForSonHtml(menu.getChildren(),menu.getPid()));
            }
            sb.append("</li>");
        }
        return sb.toString();
    }

    private final String MenuForSonHtml(final List<Menu> list,final String pid){
        final StringBuilder sb = new StringBuilder();
        sb.append("<ul class=\"submenu\">");
        for (int i = 0; i < list.size(); i++){
            final Menu menu = list.get(i);
            sb.append("<li class=\"\">");
            final Integer subset = menu.getSubset();
            if(subset == 1){
                sb.append("<a href=\"javascript:;\" class=\"dropdown-toggle\">");
            }else{
                sb.append("<a data-url=\""+ menu.getUrl()+"\" href=\"#" + menu.getUrl() + "\" >");
            }
            if(pid.equalsIgnoreCase(ConfigFile.pid)){//有且只有第一级菜单有‘<’图标,即最顶级的菜单且还有子菜单
                sb.append("<i class=\"menu-icon fa fa-caret-right\"></i>");
            }
            sb.append(menu.getName());
            final int size = menu.getChildren().size();
            if(size > 0){
                sb.append("<b class=\"arrow fa fa-angle-down\"></b>");
            }
            sb.append("</a>");
            sb.append("<b class=\"arrow\"></b>");
            if(size > 0){
                sb.append(MenuForSonHtml(menu.getChildren(),menu.getPid()));
            }
            sb.append("</li>");
        }
        sb.append("</ul>");
        return sb.toString();
    }
}