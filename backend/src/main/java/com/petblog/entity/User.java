package com.petblog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户实体类 - 对应数据库 user 表
 * 包含用户的基本信息：用户名、密码、昵称、头像等
 */
@Data
@TableName("user")
public class User {

    /** 用户ID（主键，自增） */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户名（登录账号） */
    private String username;

    /** 密码（存储加密后的密码） */
    private String password;

    /** 昵称（用户显示名称） */
    private String nickname;

    /** 头像URL */
    private String avatar;

    /** 邮箱 */
    private String email;

    /** 手机号 */
    private String phone;

    /** 个人简介 */
    private String bio;

    /** 角色：0-普通用户，1-管理员 */
    private Integer role;

    /** 逻辑删除：0-未删除，1-已删除 */
    @TableLogic
    private Integer deleted;

    /** 创建时间（自动填充） */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间（自动填充） */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
