package com.petblog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 举报实体类 - 对应数据库 report 表
 * 记录用户对违规文章的举报信息
 */
@Data
@TableName("report")
public class Report {

    /** 举报ID（主键，自增） */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 被举报的文章ID */
    private Long articleId;

    /** 举报者ID */
    private Long userId;

    /** 举报原因 */
    private String reason;

    /** 处理状态：0-待处理，1-已处理 */
    private Integer status;

    /** 处理结果说明 */
    private String result;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 处理时间 */
    private LocalDateTime handleTime;

    /** 被举报文章标题（非数据库字段） */
    @TableField(exist = false)
    private String articleTitle;

    /** 举报者昵称（非数据库字段） */
    @TableField(exist = false)
    private String userName;
}
