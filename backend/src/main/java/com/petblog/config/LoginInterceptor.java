package com.petblog.config;

import com.petblog.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器
 * 用于验证用户是否已登录（Token 是否有效）
 * 
 * 工作流程：
 * 1. 从请求头中获取 Authorization 字段
 * 2. 检查 Token 是否存在且格式正确（Bearer xxx）
 * 3. 验证 Token 是否有效（未过期、未被篡改）
 * 4. 将用户ID存入 request 属性中，方便后续使用
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 预检请求直接放行（CORS跨域）
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 从请求头获取 Token
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"未登录，请先登录\"}");
            return false;
        }

        // 提取并验证 Token
        String token = authHeader.substring(7);
        try {
            if (!jwtUtil.validateToken(token)) {
                response.setStatus(401);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":401,\"message\":\"登录已过期，请重新登录\"}");
                return false;
            }
            // 将用户信息存入 request，后续可通过 request.getAttribute("userId") 获取
            Long userId = jwtUtil.getUserId(token);
            request.setAttribute("userId", userId);
            request.setAttribute("username", jwtUtil.getUsername(token));
            return true;
        } catch (Exception e) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"Token无效\"}");
            return false;
        }
    }
}
