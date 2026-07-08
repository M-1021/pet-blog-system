package com.petblog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 点赞实体类 - 对应数据库 article_like 表
 * 记录用户对文章的点赞关系
 */
@Data
@TableName("article_like")
public class ArticleLike {

    /** 点赞ID（主键，自增） */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 文章ID */
    private Long articleId;

    /** 用户ID */
    private Long userId;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
