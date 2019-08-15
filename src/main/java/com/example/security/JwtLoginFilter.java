package com.example.security;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.utils.JwtTokenUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 *
 * 启动登录认证流程过滤器
 *
 * @author 程思琦
 * @date 2019/8/15 16:06
 * @description 登录认证触发过滤器
 *
 * JwtLoginFilter 是在通过访问 /login 的POST请求时首先被触发的过滤器,
 * 默认实现是UsernamePasswordAuthenticationFilter,它继承了AbstractAuthenticationProcessingFilter,
 * 抽象父类的 doFilter 定义了登录认证的大致操作流程,这里我们的JwtLoginFilter 继承了UsernamePasswordAuthenticationFilter,
 * 并进行了两个主要内容的定制。
 *
 * 1、覆写认证方法,修改用户名、密码的获取方式,具体原因看代码注释
 * 2、覆写认证成功后的操作,移除后台跳转,添加生成令牌并返回给客户端
 *
 *
 * ====================================================
 * 除了使用上面的登录认证过滤器拦截 /login POST请求之外, 我们也可以
 * 不使用上面的过滤器的这种方式, 通过自定义登录接口实现, 只要在登录接口手动触发登录流程并生产令牌即可。
 * 详情： 查看LoginController类
 *
 *
 * 其实 Spring Security的登录认证过程只需要调用AuthenticationManager的 authenticate(Authentication auth)方法,
 * 最终返回认证成功的 Authentication实现类并存储到SpringContextHolder 上下文即可, 这样后面授权的时候就可以
 * 从SpringContextHolder中获取登录认证信息, 并根据其中的用户信息和权限信息决定是否进行授权
 *
 *
 **/
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {

    public JwtLoginFilter(AuthenticationManager authenticationManager){
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response
            , FilterChain chain)throws IOException, ServletException {
        // POST请求 /login 登录时拦截, 由此方法触发执行登录认证流程,可以在此覆写整个登录认证逻辑
        super.doFilter(request, response, chain);
    }

    /**
     * 认证方法
     */
    @Override
    public Authentication attemtAuthentication(HttpServletRequest request
            , HttpServletResponse response) throws AuthenticationException {
        /**
         * 可以在此覆写尝试进行登录认证的逻辑, 登录成功之后等操作不在此方法内
         * 如果使用此过滤器来触发登录认证流程, 注意登录请求数据格式的问题
         * 此过滤器的用户名密码默认从 request.getParameter() 获取, 但是这种
         * 读取方式不能读取到如 application/json等 post请求数据, 需要把
         * 用户名密码的读取逻辑修改为到流中读取 request.getInputStream()
         */
        String body = getBody(request);
        // 解析json
        JSONObject jsonObject = JSON.parseObject(body);
        // 获取 用户名密码
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");

        JwtAuthenticationToken authRequest = new JwtAuthenticationToken(username, password);

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * 认证成功后的操作
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response
            , FilterChain chain, Authentication authResult) throws  IOException, ServletException{
        // 存储登录认证信息到上下文
        SecurityContextHolder.getContext().setAuthentication(authResult);

        // 记住我服务
        getRememberMeServices().loginSuccess(request, response, authResult);

        // 触发事件监听器
        if(this.eventPublisher != null){
            eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(authResult, this.getClass()));
        }

        // 生成并返回 token 给客户端, 后续访问携带此 token
        JwtAuthenticationToken token = new JwtAuthenticationToken(null, null
                , JwtTokenUtils.generateToken(authResult));

        HttpUtils.write(response, token);
    }

    /**
     * 获取请求Body 从流中读取
     * @author 程思琦
     * @date 16:29 2019/8/15
     * @param request 请求
     * @return String
     */
    public String getBody(HttpServletRequest request){
        StringBuilder sb = new StringBuilder();
        InputStream is = null;
        BufferedReader br = null;

        try {
            is = request.getInputStream();
            // 缓冲字符输入流
            br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

            String line ;
            while ((line = br.readLine()) != null ){
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭 流资源
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}
