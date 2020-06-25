package com.fwtai.service;

import com.fwtai.bean.JwtUser;
import com.fwtai.bean.SysUser;
import com.fwtai.service.web.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 登录处理,实现登录认证及权限鉴权,主要职责就是认证用户名和密码是否和该用户的权限角色集合
*/
@Service
public class UserServiceDetails implements UserDetailsService{

    @Autowired
    private UserService userService;

    @Resource
    private AsyncService asyncService;

    /**
     * 通过账号查找用户信息,用于登录
     * @param username
     * @return
     * @throws UsernameNotFoundException
    */
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException{
        final SysUser user = userService.getUserByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }else {//处理锁定的问题
            final Integer enabled = user.getEnabled();
            if(enabled == 0){
                asyncService.updateLogin(username);
            }
            return new JwtUser(user.getKid(),user.getUserName(),user.getUserPassword(),enabled);
        }
    }
    /*

     其实可以使用security提供的类来返回

     public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException{
        final SqlSessionTemplate sqlSession = dao.getSqlSession();
        final List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        final HashMap<String,String> user = dao.queryForEntity("");//查询用户
        if(user != null){
            //查询权限
            final List<HashMap<String,Object>> lists = dao.queryForListHashMap("");
            lists.forEach(list -> {
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(String.valueOf(list.get("permission")));
                grantedAuthorities.add(grantedAuthority);
            });
        }
        final List<HashMap<String,Object>> list = dao.queryForListHashMap("");
        return new org.springframework.security.core.userdetails.User(username,user.get("password"),grantedAuthorities);
    }

     */

    /**
     * 通过userId动态获取用户的全部角色和权限的信息
     * @param
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2020/5/1 0:49
    */
    public UserDetails getUserById(final String userId){
        final SysUser user = userService.getUserById(userId);
        if(user != null){
            final List<String> roles =  userService.getRolePermissions(userId);
            final List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            for (final String role : roles){
                authorities.add(new SimpleGrantedAuthority(role));
            }
            return new JwtUser(user.getKid(),user.getUserName(),user.getUserPassword(),user.getEnabled(),authorities);
        }
        throw new UsernameNotFoundException("账号信息不存在");
    }
}