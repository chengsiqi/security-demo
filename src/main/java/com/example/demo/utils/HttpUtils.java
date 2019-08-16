package com.example.demo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.security.JwtAuthenticationToken;
import com.example.demo.vo.HttpResult;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * HTTP 工具类
 *
 * @author 程思琦
 * @date 2019/8/16 17:31
 * @description
 **/
public class HttpUtils {


    /**
     * 输出信息到浏览器
     *
     * @author 程思琦
     * @date 17:40 2019/8/16
     * @param response
     * @param data
     */
    public static void write(HttpServletResponse response, Object data)throws IOException {
        // 设置响应的数据格式
        response.setContentType("application/json; charset=utf-8");
        HttpResult result = HttpResult.ok(data);
        String json = JSONObject.toJSONString(result);
        response.getWriter().print(json);
        response.getWriter().flush();
        response.getWriter().close();
    }
}
