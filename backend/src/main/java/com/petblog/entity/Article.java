package com.petblog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 宠物知识文章实体类 - 对应数据库 article 表
 * 存储宠物知识文章的基本信息
 */
@Data
@TableName("article")
public class Article {

    /** 文章ID（主键，自增） */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 文章标题 */
    private String title;

    /** 文章内容 */
    private String content;

    /** 文章封面图片URL */
    private String coverImage;

    /** 所属分类ID */
    private Long categoryId;

    /** 作者ID */
    private Long userId;

    /** 点赞数 */
    private Integer likeCount;

    /** 评论数 */
    private Integer commentCount;

    /** 浏览量 */
    private Integer viewCount;

    /** 状态：0-正常，1-违规下架 */
    private Integer status;

    /** 逻辑删除 */
    @TableLogic
    private Integer deleted;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 作者昵称（非数据库字段，用于联查显示） */
    @TableField(exist = false)
    private String authorName;

    /** 分类名称（非数据库字段，用于联查显示） */
    @TableField(exist = false)
    private String categoryName;
}
