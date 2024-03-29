package com.example.demo.utils;

import com.example.demo.security.JwtAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;

/**
 * 如下是 系统登录认证的逻辑, 可以看到部分逻辑和登录认证过滤器的差不多。
 * 1、执行登录认证过程, 通过调用AuthenticationManager 的 authenticate(token) 方法实现
 * 2、将认证成功的认证信息存储到上下文, 供后续访问授权的时候获取使用
 * 3、通过JWT生成令牌并返回给客户端, 后续访问和操作都需要携带此令牌
 *
 *
 * @author 程思琦
 * @date 2019/8/15 17:33
 * @description
 **/
public class SecurityUtils {

    private SecurityUtils(){
    }

    /**
     * 系统登录认证
     * @author 程思琦
     * @date 17:56 2019/8/15
     * @param request 请求
     * @param username 用户名
     * @param password 密码
     * @param authenticationManager 认证管理员
     * @return JwtAuthenticationToken
     */
    public static JwtAuthenticationToken login(HttpServletRequest request, String username, String password
            , AuthenticationManager authenticationManager){

        JwtAuthenticationToken authRequest = new JwtAuthenticationToken(username, password);

        authRequest.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // 执行登录认证过程
        Authentication authentication = authenticationManager.authenticate(authRequest);

        // 认证成功存储认证信息到上下文
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 生成令牌并返回给客户端
        authRequest.setToken(JwtTokenUtils.generateToken(authentication));

        return authRequest;
    }

    /**
     * 获取令牌进行验证
     *
     * @author 程思琦
     * @date 15:51 2019/8/16
     * @param request
     * @return
     */
    public static void checkAuthentication(HttpServletRequest request){
        // 获取令牌并根据令牌获得登录认证信息
        Authentication authentication = JwtTokenUtils.getAuthenticationFromToken(request);
        // 设置登录认证信息到上下文
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * 获取当前用户名
     * @author 程思琦
     * @date 10:47 2019/8/16
     * @return String
     */
    public static String getUsername(){
        Authentication authentication = getAuthentication();
        return getUsername(authentication);
    }

    /**
     * 获取用户名
     * @author 程思琦
     * @date 10:47 2019/8/16
     * @param authentication
     * @return String
     */
    public static String getUsername(Authentication authentication){
        String username = null;
        if(authentication != null){
            Object principal = authentication.getPrincipal();
            if(principal != null && principal instanceof UserDetails){
                username = ((UserDetails) principal).getUsername();
            }
        }
        return username;
    }

    /**
     * 获取当前登录信息
     * @author 程思琦
     * @date 15:54 2019/8/16
     * @return
     */
    public static Authentication getAuthentication(){
        if(SecurityContextHolder.getContext() == null){
            return null;
        }
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
