package com.petblog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.petblog.dto.LoginDTO;
import com.petblog.dto.RegisterDTO;
import com.petblog.entity.User;

/**
 * 用户服务接口
 * 提供用户注册、登录、信息管理等功能
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param dto 注册信息DTO
     * @return 注册成功的用户ID
     */
    String register(RegisterDTO dto);

    /**
     * 用户登录
     * @param dto 登录信息DTO
     * @return JWT token字符串
     */
    String login(LoginDTO dto);

    /**
     * 根据ID获取用户信息
     * @param userId 用户ID
     * @return 用户信息（不含密码）
     */
    User getUserInfo(Long userId);

    /**
     * 更新用户信息
     * @param userId 用户ID
     * @param user 用户信息
     */
    void updateUser(Long userId, User user);

    /**
     * 获取用户列表（管理员功能）
     * @param page 页码
     * @param size 每页数量
     * @param keyword 搜索关键词
     * @return 分页用户列表
     */
    IPage<User> getUserList(int page, int size, String keyword);

    /**
     * 删除用户（管理员功能）
     * @param userId 用户ID
     */
    void deleteUser(Long userId);
}
