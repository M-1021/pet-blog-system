package com.petblog.controller;

import com.petblog.entity.Comment;
import com.petblog.service.CommentService;
import com.petblog.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 评论控制器
 * 处理评论相关的HTTP请求
 */
@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 获取文章的评论列表（公开接口）
     * @param articleId 文章ID
     * @return 评论列表
     */
    @GetMapping("/list/{articleId}")
    public Result getCommentsByArticle(@PathVariable Long articleId) {
        List<Comment> comments = commentService.getCommentsByArticle(articleId);
        return Result.success(comments);
    }

    /**
     * 添加评论（需要登录）
     * @param request HTTP请求
     * @param comment 评论内容
     * @return 添加结果
     */
    @PostMapping
    public Result addComment(HttpServletRequest request, @RequestBody Comment comment) {
        Long userId = (Long) request.getAttribute("userId");
        commentService.addComment(userId, comment);
        return Result.success("评论成功");
    }

    /**
     * 删除评论（需要登录）
     * @param request HTTP请求
     * @param id 评论ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result deleteComment(HttpServletRequest request, @PathVariable Long id) {
        Long userId = (Long) request.getAttribute("userId");
        commentService.deleteComment(userId, id);
        return Result.success("删除成功");
    }
}
