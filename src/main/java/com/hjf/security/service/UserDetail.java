package com.hjf.security.service;

import com.hjf.security.dao.UserDao;
import com.hjf.security.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * DaoAuthenticationProvider 类中包含了这个UserDetailsService这个接口,我们可以实现这个接口来达到
 * 我们从内存或数据库得到我们的用户名,密码,还有权限或角色;
 *
 * 如果 WebSecurityConfig 类中的 得到用户和这里同时开启,会使用WebSecurityConfig中的
 */
@Service
public class UserDetail implements UserDetailsService {


    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserDao userDao;

    //得到UserDetails
/*    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        System.out.println(userName);
        //密码在test目录下有测试方法
        UserDto userDto = userDao.getUserByUsername(userName);
        return User.withUsername("hjf").password(passwordEncoder.encode("123")).authorities("p2").build();
    }*/

    //基于数据库得到UserDetails
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        System.out.println(userName);
        //密码在test目录下有测试方法
        UserDto userDto = userDao.getUserByUsername(userName);
        if(userDto == null){
            //如果查不到就返回null,让DaoAuthenticationProvider内部统一报异常
            return null;
        }
        //根据用户id查询用户权限
        List<String> permissions = userDao.findPermissionsByUserId(userDto.getId().toString());
        String[] perArray = new String[permissions.size()];
        permissions.toArray(perArray);
        return User.withUsername(userDto.getUsername()).password(userDto.getPassword()).authorities(perArray).build();
    }


}
