package com.petblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.petblog.dto.LoginDTO;
import com.petblog.dto.RegisterDTO;
import com.petblog.entity.User;
import com.petblog.mapper.UserMapper;
import com.petblog.service.UserService;
import com.petblog.util.JwtUtil;
import com.petblog.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类
 * 处理用户注册、登录、信息管理等业务逻辑
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 用户注册
     * 检查用户名是否已存在，加密密码后保存用户
     */
    @Override
    public String register(RegisterDTO dto) {
        // 检查用户名是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, dto.getUsername());
        if (this.count(wrapper) > 0) {
            throw new RuntimeException("用户名已存在");
        }

        // 创建用户对象
        User user = new User();
        user.setUsername(dto.getUsername());
        // 使用密码工具类加密密码
        user.setPassword(PasswordUtil.encrypt(dto.getPassword()));
        user.setNickname(dto.getNickname());
        user.setRole(0); // 0表示普通用户

        // 保存用户
        this.save(user);
        return user.getId().toString();
    }

    /**
     * 用户登录
     * 验证用户名密码，生成JWT token
     */
    @Override
    public String login(LoginDTO dto) {
        // 根据用户名查询用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, dto.getUsername());
        User user = this.getOne(wrapper);

        // 验证用户是否存在
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 验证密码
        if (!PasswordUtil.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 生成JWT token
        return jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
    }

    /**
     * 获取用户信息
     * 返回用户信息，排除密码字段
     */
    @Override
    public User getUserInfo(Long userId) {
        User user = this.getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        // 清除密码字段，不返回给前端
        user.setPassword(null);
        return user;
    }

    /**
     * 更新用户信息
     * 只更新允许修改的字段：昵称、头像、邮箱、手机、简介
     */
    @Override
    public void updateUser(Long userId, User user) {
        User existingUser = this.getById(userId);
        if (existingUser == null) {
            throw new RuntimeException("用户不存在");
        }

        // 只更新允许修改的字段
        existingUser.setNickname(user.getNickname());
        existingUser.setAvatar(user.getAvatar());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhone(user.getPhone());
        existingUser.setBio(user.getBio());

        this.updateById(existingUser);
    }

    /**
     * 获取用户列表（管理员功能）
     * 支持按用户名或昵称搜索
     */
    @Override
    public IPage<User> getUserList(int page, int size, String keyword) {
        Page<User> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();

        // 如果有关键词，按用户名或昵称模糊搜索
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(User::getUsername, keyword)
                    .or()
                    .like(User::getNickname, keyword);
        }
        wrapper.orderByDesc(User::getCreateTime);

        IPage<User> result = this.page(pageParam, wrapper);
        // 清除所有用户密码
        result.getRecords().forEach(u -> u.setPassword(null));
        return result;
    }

    /**
     * 删除用户（管理员功能）
     */
    @Override
    public void deleteUser(Long userId) {
        if (!this.removeById(userId)) {
            throw new RuntimeException("删除失败，用户不存在");
        }
    }
}
