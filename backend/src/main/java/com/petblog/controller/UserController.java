package com.petblog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.petblog.dto.LoginDTO;
import com.petblog.dto.RegisterDTO;
import com.petblog.entity.User;
import com.petblog.service.UserService;
import com.petblog.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 用户控制器
 * 处理用户相关的HTTP请求，包括注册、登录、信息管理等
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     * @param dto 注册信息
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result register(@Valid @RequestBody RegisterDTO dto) {
        String userId = userService.register(dto);
        return Result.success("注册成功", userId);
    }

    /**
     * 用户登录
     * @param dto 登录信息
     * @return 登录成功返回token
     */
    @PostMapping("/login")
    public Result login(@Valid @RequestBody LoginDTO dto) {
        String token = userService.login(dto);
        return Result.success("登录成功", token);
    }

    /**
     * 获取当前用户信息
     * 从请求中获取当前登录用户的ID
     * @param request HTTP请求
     * @return 用户信息
     */
    @GetMapping("/info")
    public Result getUserInfo(HttpServletRequest request) {
        // 从请求属性中获取用户ID（由JWT拦截器设置）
        Long userId = (Long) request.getAttribute("userId");
        User user = userService.getUserInfo(userId);
        return Result.success(user);
    }

    /**
     * 更新用户信息
     * @param request HTTP请求
     * @param user 用户信息
     * @return 更新结果
     */
    @PutMapping("/info")
    public Result updateUser(HttpServletRequest request, @RequestBody User user) {
        Long userId = (Long) request.getAttribute("userId");
        userService.updateUser(userId, user);
        return Result.success("更新成功");
    }

    /**
     * 获取用户列表（管理员功能）
     * @param page 页码
     * @param size 每页数量
     * @param keyword 搜索关键词
     * @return 分页用户列表
     */
    @GetMapping("/list")
    public Result getUserList(@RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "10") int size,
                             @RequestParam(required = false) String keyword) {
        IPage<User> userPage = userService.getUserList(page, size, keyword);
        return Result.success(userPage);
    }

    /**
     * 删除用户（管理员功能）
     * @param id 用户ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success("删除成功");
    }
}
