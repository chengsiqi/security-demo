package com.example.demo.config;

import com.example.demo.security.JwtAuthenticationFilter;
import com.example.demo.security.JwtAuthenticationProvider;
import com.example.demo.security.JwtLoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

/**
 *
 * Authentication   认证
 * Authorization    授权
 *
 * @author 程思琦
 * @date 2019/8/15 11:29
 * @description 安全配置类
 *
 * 这个配置类是Spring Security的关键配置
 *
 * 在这个配置类中,主要做了一下几个配置：
 * 1、访问路径URL的授权策略,如登陆、Swagger访问免登陆认证等
 * 2、指定了登录认证流程过滤器JwtLoginFilter,由它来触发登录认证
 * 3、指定了自定义身份认证组件JwtAuthenticationProvider,并注入UserDetailsService
 * 4、指定了访问控制过滤器JwtAuthenticationFilter,在授权时解析令牌和设置登录状态
 * 5、指定了退出登录处理器,因为是前后端分离,防止内置的登录处理器在后台进行跳转
 *
 *
 * Spring Security默认是禁用注解的，想要开启注解，需要在继承
 * WebSecurityConfigurerAdapter的类上加 @EnableGlobalMethodSecurity注解来判断
 * 用户对某个控制层的方法是否具有访问权限
 *
 * --@EnableGlobalMethodSecurity(prePostEnabled = true) 使用表达式时间方法级别的安全性 有4个注解可用
 *  --@PreAuthorize 在方法调用之前,基于表达式的计算结果来限制对方法的访问
 *  --@PostAuthorize 允许方法调用,但是如果表达式计算结果为false,将抛出一个安全性异常
 *  --@PostFilter 允许方法调用,但是必须按照表达式来过滤方法结果
 *  --@PreFilter 允许方法调用,但必须在进入方法之前过滤输入值
 **/
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        // 使用自定义登录身份认证组件
        auth.authenticationProvider(new JwtAuthenticationProvider(userDetailsService));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 禁用 csrf, 由于使用的是JWT, 我们这里不需要csrf
        http.cors().and().csrf().disable()
                .authorizeRequests()
                // 跨域预检请求
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // 登录URL
                .antMatchers("/login").permitAll()
                // swagger
                .antMatchers("/swagger**/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/v2/**").permitAll()
                // 其它所有请求需要身份认证
                .anyRequest().authenticated();

        // 退出登录处理器
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

        /**
         * 注意： 如果使用LoginController 登录控制器触发登录认证操作, 需要禁用登录认证过滤器 JwtLoginFilter ,
         * 即将 WebSecurityConfig 中的以下配置项注释即可, 否则访问LoginController中的登录接口会被过滤拦截, 执行不会进入LoginController 中的登录接口
         */
        // 开启登录认证流程过滤器, 如果使用LoginController的login接口, 需要注释掉此过滤器，根据使用习惯二选一即可
        /*http.addFilterBefore(new JwtLoginFilter(authenticationManager()),
                UsernamePasswordAuthenticationFilter.class);*/

        // 访问控制时登录状态检查过滤器
        http.addFilterBefore(new JwtAuthenticationFilter(authenticationManager()),
                UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception{
        return super.authenticationManager();
    }
}
