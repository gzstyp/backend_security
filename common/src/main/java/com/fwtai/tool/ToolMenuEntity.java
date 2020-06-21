package com.fwtai.tool;

import com.fwtai.bean.Menu;

import java.util.ArrayList;
import java.util.List;

/**
 * 组装无限极导航菜单,bean实现,final
 * @用法 final List<Menu> rootMenu = new ToolMenuEntity().initMenu(lists,ConfigFile.pid);//pid是32个8,死值
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2018-06-02 10:02
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
*/
public final class ToolMenuEntity{

    /**组装菜单数据,用法：final List<Menu> rootMenu = new ToolMenuEntity().initMenu(lists,ConfigFile.pid);pid是32个8,死值 */
    public List<Menu> initMenu(final List<Menu> allMenu,final String pid){
        final List<Menu> rootMenu = new ArrayList<Menu>();
        for (final Menu menu : allMenu){
            //父节点是topId为最外层的根节点。
            if(menu.getPid().equals(pid)){
                rootMenu.add(menu);
            }
        }
        //为根菜单设置子菜单，getClild是递归调用的
        for (final Menu menu : rootMenu){
            /* 获取根节点下的所有子节点 使用getChild方法*/
            final List<Menu> children = getChild(menu.getKid(),allMenu);
            menu.setChildren(children);
        }
        return rootMenu;
    }

    /**
     * 获取子节点
     * @param id 父节点id
     * @param allMenu 所有菜单列表
     * @return 每个根节点下，所有子菜单列表
    */
    private final List<Menu> getChild(final String id,final List<Menu> allMenu){
        //子菜单
        final List<Menu> childList = new ArrayList<Menu>();
        for (final Menu menu : allMenu) {
            // 遍历所有节点，将所有菜单的父id与传过来的根节点的id比较,相等说明：为该根节点的子节点。
            if(menu.getPid().equals(id)){
                childList.add(menu);
            }
        }
        //递归
        for (final Menu menu : childList) {
            final List<Menu> child = getChild(menu.getKid(),allMenu);
            menu.setChildren(child);
        }
        return childList;
    }
}