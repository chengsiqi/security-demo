package com.example.demo.security;

import com.example.demo.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * 登录认证检查过滤器
 *
 * 访问接口的时候, 登录认证检查过滤器JwtAuthenticationFilter 会拦截
 * 请求并检验令牌和登录状态, 并根据情况设置登录状态
 *
 * @author 程思琦
 * @date 2019/8/16 15:17
 * @description
 **/
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    @Autowired
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response
            , FilterChain chain) throws IOException, ServletException {
        // 获取token, 并检查登录状态
        SecurityUtils.checkAuthentication(request);
        chain.doFilter(request, response);
    }

}
