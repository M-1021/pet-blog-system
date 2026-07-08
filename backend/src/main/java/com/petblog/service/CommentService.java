package com.petblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.petblog.entity.Comment;

import java.util.List;

/**
 * 评论服务接口
 * 提供评论的查询、添加和删除功能
 */
public interface CommentService extends IService<Comment> {

    /**
     * 获取文章的评论列表
     * @param articleId 文章ID
     * @return 评论列表（包含用户名）
     */
    List<Comment> getCommentsByArticle(Long articleId);

    /**
     * 添加评论
     * @param userId 用户ID
     * @param comment 评论内容
     */
    void addComment(Long userId, Comment comment);

    /**
     * 删除评论
     * @param userId 用户ID（用于权限验证）
     * @param commentId 评论ID
     */
    void deleteComment(Long userId, Long commentId);
}
