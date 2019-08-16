package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 程思琦
 * @date 2019/8/15 11:12
 * @description 添加CORS跨域配置类
 **/
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry){
        // 允许跨域访问的路径
        registry.addMapping("/**")
                // 允许跨域访问的源
                .allowedOrigins("*")
                // 允许请求方法
                .allowedMethods("POST","GET","PUT","OPTIONS","DELETE")
                // 预检时间间隔
                .maxAge(168000)
                // 允许头部设置
                .allowedHeaders("*")
                // 是否发送cookie
                .allowCredentials(true);
    }
}
