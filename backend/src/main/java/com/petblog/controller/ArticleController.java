package com.petblog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.petblog.entity.Article;
import com.petblog.service.ArticleService;
import com.petblog.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 文章控制器
 * 处理文章相关的HTTP请求，包括文章的增删改查和点赞
 */
@RestController
@RequestMapping("/api/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 获取文章列表（公开接口）
     * @param page 页码
     * @param size 每页数量
     * @param categoryId 分类ID（可选）
     * @param keyword 搜索关键词（可选）
     * @return 分页文章列表
     */
    @GetMapping("/list")
    public Result getArticleList(@RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "10") int size,
                                @RequestParam(required = false) Long categoryId,
                                @RequestParam(required = false) String keyword) {
        IPage<Article> articlePage = articleService.getArticleList(page, size, categoryId, keyword);
        return Result.success(articlePage);
    }

    /**
     * 获取文章详情（公开接口）
     * @param id 文章ID
     * @return 文章详情
     */
    @GetMapping("/detail/{id}")
    public Result getArticleDetail(@PathVariable Long id) {
        Article article = articleService.getArticleDetail(id);
        return Result.success(article);
    }

    /**
     * 创建文章（需要登录）
     * @param request HTTP请求
     * @param article 文章信息
     * @return 创建结果
     */
    @PostMapping
    public Result createArticle(HttpServletRequest request, @RequestBody Article article) {
        Long userId = (Long) request.getAttribute("userId");
        articleService.createArticle(userId, article);
        return Result.success("发布成功");
    }

    /**
     * 更新文章（需要登录）
     * @param request HTTP请求
     * @param article 文章信息
     * @return 更新结果
     */
    @PutMapping
    public Result updateArticle(HttpServletRequest request, @RequestBody Article article) {
        Long userId = (Long) request.getAttribute("userId");
        articleService.updateArticle(userId, article);
        return Result.success("更新成功");
    }

    /**
     * 删除文章（需要登录）
     * @param request HTTP请求
     * @param id 文章ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result deleteArticle(HttpServletRequest request, @PathVariable Long id) {
        Long userId = (Long) request.getAttribute("userId");
        articleService.deleteArticle(userId, id);
        return Result.success("删除成功");
    }

    /**
     * 点赞/取消点赞文章（需要登录）
     * @param request HTTP请求
     * @param id 文章ID
     * @return 点赞状态
     */
    @PostMapping("/like/{id}")
    public Result toggleLike(HttpServletRequest request, @PathVariable Long id) {
        Long userId = (Long) request.getAttribute("userId");
        boolean liked = articleService.toggleLike(userId, id);
        return Result.success(liked ? "点赞成功" : "取消点赞成功", liked);
    }

    /**
     * 获取我的文章列表（需要登录）
     * @param request HTTP请求
     * @return 文章列表
     */
    @GetMapping("/my")
    public Result getMyArticles(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        List<Article> articles = articleService.getMyArticles(userId);
        return Result.success(articles);
    }
}
