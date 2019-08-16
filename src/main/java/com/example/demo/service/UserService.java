package com.example.demo.service;

import com.example.demo.model.User;

import java.util.Set;

/**
 * 用户管理
 *
 * @author： 程思琦
 * @date ：2019/8/16 14:57
 * @description：
 **/
public interface UserService {

    /**
     * 根据用户名查询用户
     * @author 程思琦
     * @date 14:58 2019/8/16
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * 查询用户的菜单权限标识集合
     * @author 程思琦
     * @date 14:58 2019/8/16
     * @param username
     * @return
     */
    Set<String> findPermissions(String username);
}
