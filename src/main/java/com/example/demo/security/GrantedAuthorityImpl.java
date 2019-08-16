package com.example.demo.security;

import org.springframework.security.core.GrantedAuthority;

/**
 * 权限封装
 * @author 程思琦
 * @date 2019/8/16 17:55
 * @description
 **/
public class GrantedAuthorityImpl implements GrantedAuthority {

    private static final long serialVersionUID = 1L;

    private String authority;

    public GrantedAuthorityImpl(String authority){
        this.authority = authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return null;
    }
}
