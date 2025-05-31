package com.util;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class AuthContextHolder {

    private static final String TOKEN_HEADER = "token";

    /**
     * 从请求头中获取 token
     */
    public String getToken(HttpServletRequest request) {
        return request.getHeader(TOKEN_HEADER);
    }

    /**
     * 从请求头 token 中获取用户 ID
     */
    public Long getUserId(HttpServletRequest request) {
        String token = getToken(request);
        if (StringUtils.hasText(token)) {
            return JwtHelper.getUserId(token);
        }
        return null;
    }

    /**
     * 从请求头 token 中获取用户名
     */
    public String getUserName(HttpServletRequest request) {
        String token = getToken(request);
        if (StringUtils.hasText(token)) {
            return JwtHelper.getUserName(token);
        }
        return null;
    }
}
