package com.example.demo.security;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * 用户登录认证信息查询
 *
 * 自定义UserDetailsService 从我们的用户服务UserService中获取用户和权限信息
 *
 * @author 程思琦
 * @date 2019/8/16 14:07
 * @description
 **/
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    /**
     * 一般而言, 定制UserDetailsService 就可以满足大部分需求了,在UserDetailsService满足不了
     * 我们的需求的时候考虑定制AuthenticationProvider
     *
     * 如果直接定制UserDetailsService, 而不自定义 AuthenticationProvider, 可以直接在配置文件 WebSecurityConfig 中这样配置
     *
     * -@Override
     * public void configure(AuthenticationManagerBuilder auth)throws Exception {
     *    // 使用自定义的获取信息获取服务
     *    auth.userDetailsService(userDetailsService);
     * }
     *
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("该用户不存在");
        }

        /**
         * 用户权限列表, 根据用户拥有的权限标识与如 @PreAuthorize("hasAuthority('sys:menu:view')")标注的
         * 接口对比, 决定是否可以调用接口
         */
        Set<String> permissions = userService.findPermissions(username);
        List<GrantedAuthority> grantedAuthorities = permissions.stream().map(GrantedAuthorityImpl::new).collect(Collectors.toList());
        return new JwtUserDetails(username, user.getPassword(), grantedAuthorities);
    }
}
