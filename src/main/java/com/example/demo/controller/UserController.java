package com.example.demo.controller;

import com.example.demo.vo.HttpResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制器, 提供三个测试接口, 其中权限列表中未包含删除接口定义
 * 的权限('sys:user:delete')登录之后也将无权限调用
 *
 * @author 程思琦
 * @date 2019/8/16 15:07
 * @description
 **/
@RestController
@RequestMapping("/user")
public class UserController {

    @PreAuthorize("hasAuthority('sys:user:view')")
    @GetMapping("/findAll")
    @ApiOperation("测试权限 查询所有用户")
    public HttpResult findAll(){
        return HttpResult.ok("this findAll service is called success.");
    }

    @PreAuthorize("hasAuthority('sys:user:edit')")
    @GetMapping("/edit")
    @ApiOperation("测试权限 编辑用户")
    public HttpResult edit(){
        return HttpResult.ok("this edit service is called success.");
    }

    @PreAuthorize("hasAuthority('sys:user:delete')")
    @GetMapping("/delete")
    @ApiOperation("查询权限 删除用户")
    public HttpResult delete(){
        return HttpResult.ok("this delete service is called success.");
    }
}
