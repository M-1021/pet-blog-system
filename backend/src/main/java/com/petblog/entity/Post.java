package com.petblog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 在线交流帖子实体类 - 对应数据库 post 表
 * 存储用户发布的在线交流帖子
 */
@Data
@TableName("post")
public class Post {

    /** 帖子ID（主键，自增） */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 帖子标题 */
    private String title;

    /** 帖子内容 */
    private String content;

    /** 发布者ID */
    private Long userId;

    /** 回复数 */
    private Integer replyCount;

    /** 逻辑删除 */
    @TableLogic
    private Integer deleted;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 发布者昵称（非数据库字段） */
    @TableField(exist = false)
    private String userName;
}
