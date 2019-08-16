package com.example.demo.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 *
 * 安全用户模型
 *
 * UserDetailsService 加载好用户认证信息后会封装认证信息到一个UserDetails的实现类
 *
 * 默认实现是User 类, 我们这里没有特殊需要，简单继承即可，复杂需求可以在此基础上进行拓展。
 *
 *
 * @author 程思琦
 * @date 2019/8/16 14:42
 * @description
 **/
public class JwtUserDetails extends User {


    public JwtUserDetails(String username, String password
            , Collection<? extends GrantedAuthority> authorities) {
        this(username, password, true, true, true, true, authorities);
    }

    public JwtUserDetails(String username, String password, boolean enabled
            , boolean accountNonExpired, boolean credentialsNonExpired
            , boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }
}
