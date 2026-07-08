package com.petblog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 帖子回复实体类 - 对应数据库 reply 表
 * 存储用户对在线交流帖子的回复
 */
@Data
@TableName("reply")
public class Reply {

    /** 回复ID（主键，自增） */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 回复内容 */
    private String content;

    /** 所属帖子ID */
    private Long postId;

    /** 回复者ID */
    private Long userId;

    /** 逻辑删除 */
    @TableLogic
    private Integer deleted;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 回复者昵称（非数据库字段） */
    @TableField(exist = false)
    private String userName;
}
