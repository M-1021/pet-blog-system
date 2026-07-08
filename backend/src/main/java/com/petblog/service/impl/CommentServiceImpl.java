package com.petblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.petblog.entity.Comment;
import com.petblog.entity.User;
import com.petblog.mapper.CommentMapper;
import com.petblog.service.CommentService;
import com.petblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 评论服务实现类
 * 处理评论的查询、添加和删除业务逻辑
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserService userService;

    /**
     * 获取文章的评论列表
     * 查询评论并填充用户名
     */
    @Override
    public List<Comment> getCommentsByArticle(Long articleId) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getArticleId, articleId)
               .orderByDesc(Comment::getCreateTime);
        List<Comment> comments = this.list(wrapper);

        // 填充用户名
        comments.forEach(this::fillUserName);
        return comments;
    }

    /**
     * 添加评论
     */
    @Override
    public void addComment(Long userId, Comment comment) {
        comment.setUserId(userId);
        comment.setCreateTime(LocalDateTime.now());
        this.save(comment);
    }

    /**
     * 删除评论
     * 只有评论作者可以删除自己的评论
     */
    @Override
    public void deleteComment(Long userId, Long commentId) {
        Comment comment = this.getById(commentId);
        if (comment == null) {
            throw new RuntimeException("评论不存在");
        }
        // 验证是否为评论作者
        if (!comment.getUserId().equals(userId)) {
            throw new RuntimeException("无权删除此评论");
        }
        this.removeById(commentId);
    }

    /**
     * 填充用户名
     * 查询评论对应的用户信息
     */
    private void fillUserName(Comment comment) {
        if (comment.getUserId() != null) {
            User user = userService.getById(comment.getUserId());
            if (user != null) {
                comment.setUserName(user.getNickname() != null ? user.getNickname() : user.getUsername());
            }
        }
    }
}
