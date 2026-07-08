package com.petblog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.petblog.entity.Post;
import com.petblog.entity.Reply;
import com.petblog.service.PostService;
import com.petblog.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 帖子控制器（社区论坛功能）
 * 处理帖子相关的HTTP请求
 */
@RestController
@RequestMapping("/api/post")
public class PostController {

    @Autowired
    private PostService postService;

    /**
     * 获取帖子列表（公开接口）
     * @param page 页码
     * @param size 每页数量
     * @param keyword 搜索关键词（可选）
     * @return 分页帖子列表
     */
    @GetMapping("/list")
    public Result getPostList(@RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "10") int size,
                             @RequestParam(required = false) String keyword) {
        IPage<Post> postPage = postService.getPostList(page, size, keyword);
        return Result.success(postPage);
    }

    /**
     * 获取帖子详情（公开接口）
     * @param id 帖子ID
     * @return 帖子详情
     */
    @GetMapping("/detail/{id}")
    public Result getPostDetail(@PathVariable Long id) {
        Post post = postService.getPostDetail(id);
        return Result.success(post);
    }

    /**
     * 创建帖子（需要登录）
     * @param request HTTP请求
     * @param post 帖子内容
     * @return 创建结果
     */
    @PostMapping
    public Result createPost(HttpServletRequest request, @RequestBody Post post) {
        Long userId = (Long) request.getAttribute("userId");
        postService.createPost(userId, post);
        return Result.success("发布成功");
    }

    /**
     * 删除帖子（需要登录）
     * @param request HTTP请求
     * @param id 帖子ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result deletePost(HttpServletRequest request, @PathVariable Long id) {
        Long userId = (Long) request.getAttribute("userId");
        postService.deletePost(userId, id);
        return Result.success("删除成功");
    }

    /**
     * 获取帖子的回复列表（公开接口）
     * @param postId 帖子ID
     * @return 回复列表
     */
    @GetMapping("/reply/{postId}")
    public Result getReplies(@PathVariable Long postId) {
        List<Reply> replies = postService.getReplies(postId);
        return Result.success(replies);
    }

    /**
     * 添加回复（需要登录）
     * @param request HTTP请求
     * @param reply 回复内容
     * @return 添加结果
     */
    @PostMapping("/reply")
    public Result addReply(HttpServletRequest request, @RequestBody Reply reply) {
        Long userId = (Long) request.getAttribute("userId");
        postService.addReply(userId, reply);
        return Result.success("回复成功");
    }
}
