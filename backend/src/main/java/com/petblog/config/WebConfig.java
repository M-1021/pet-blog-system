package com.petblog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web 配置类
 * 1. 配置跨域访问（允许前端 Vue 开发服务器访问后端 API）
 * 2. 注册登录拦截器（对需要登录的接口进行拦截验证）
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    /**
     * 配置跨域访问
     * 开发阶段允许所有来源访问，生产环境应限制为具体域名
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")  // 允许所有来源
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // 允许的HTTP方法
                .allowedHeaders("*")  // 允许所有请求头
                .allowCredentials(true)  // 允许携带凭证
                .maxAge(3600);  // 预检请求缓存时间（秒）
    }

    /**
     * 注册拦截器
     * 配置哪些路径需要登录验证，哪些路径直接放行
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/api/**")        // 拦截所有 /api/ 开头的接口
                .excludePathPatterns(              // 排除不需要登录的接口
                        "/api/user/login",         // 登录接口
                        "/api/user/register",      // 注册接口
                        "/api/article/list",       // 文章列表（公开）
                        "/api/article/detail/**",  // 文章详情（公开）
                        "/api/article/like/**",    // 文章点赞（公开）
                        "/api/category/list",      // 分类列表（公开）
                        "/api/post/list",          // 帖子列表（公开）
                        "/api/post/detail/**"      // 帖子详情（公开）
                );
    }
}
