package com.example.demo.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 身份验证提供者
 *
 * @author 程思琦
 * @date 2019/8/16 11:10
 * @description
 *
 *
 * 登录身份认证组件
 *
 * 登录认证是通过调用AuthenticationManager的 authenticate(token) 方法实现的,
 * 而AuthenticationManager 又是通过调用AuthenticationProvider的 authenticate(Authentication auth)
 * 来完成认证的, 所以通过定制AuthenticationProvider 也可以完成各种自定义的需求,
 * 这里只是简单继承DaoAuthenticationProvider展示如何自定义
 *
 **/
public class JwtAuthenticationProvider extends DaoAuthenticationProvider {

    public JwtAuthenticationProvider(UserDetailsService userDetailsService){
        setUserDetailsService(userDetailsService);
        // 设置密码编码
        setPasswordEncoder(new BCryptPasswordEncoder());
    }

    /**
     * 认证信息获取服务
     *
     * 通过跟踪代码运行，我们发现像默认使用的 DaoAuthenticationProvider，在认证的使用都是通过一个叫 UserDetailsService 的来获取用户认证所需信息的。
     *
     * AbstractUserDetailsAuthenticationProvider 定义了在 authenticate 方法中通过 retrieveUser 方法获取用户信息,
     * 子类 DaoAuthenticationProvider 通过 UserDetailsService 来进行获取,
     * 一般情况, 这个UserDetailsService需要我们自定义, 实现从用户服务获取用户和权限信息封装到 UserDetails 的实现类。
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        /**
         * 可以在此处覆写整个登录认证逻辑
         */
        return super.authenticate(authentication);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails
            , UsernamePasswordAuthenticationToken authenticationToken) throws AuthenticationException{
        /**
         * 可以在此处覆写密码验证逻辑
         */
        super.additionalAuthenticationChecks(userDetails,authenticationToken);
    }

}
