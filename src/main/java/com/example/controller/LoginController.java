package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 登录控制器
 *
 * @author 程思琦
 * @date 2019/8/15 17:17
 **/
@RestController
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 登录接口
     */
    @PostMapping("/login")
    public HttpResult login(@RequestBody LoginBean loginBean, HttpServletRequest request) throws IOException {
        String username = loginBean.getUsername();
        String password = loginBean.getPassword();

        // 系统登录认证
        JwtAuthenticationToken token = SecurityUtils.login(request, username, password, authenticationManager);

        return HttpResult.ok(token);
    }
}
