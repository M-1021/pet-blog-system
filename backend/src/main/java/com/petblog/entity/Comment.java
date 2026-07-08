package com.petblog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 评论实体类 - 对应数据库 comment 表
 * 存储用户对文章的评论信息
 */
@Data
@TableName("comment")
public class Comment {

    /** 评论ID（主键，自增） */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 评论内容 */
    private String content;

    /** 所属文章ID */
    private Long articleId;

    /** 评论者ID */
    private Long userId;

    /** 父评论ID（用于回复功能，顶级评论为null） */
    private Long parentId;

    /** 逻辑删除 */
    @TableLogic
    private Integer deleted;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 评论者昵称（非数据库字段） */
    @TableField(exist = false)
    private String userName;

    /** 被回复者昵称（非数据库字段） */
    @TableField(exist = false)
    private String replyToName;
}
