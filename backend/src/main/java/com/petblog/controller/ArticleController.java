package com.petblog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.petblog.entity.Article;
import com.petblog.service.ArticleService;
import com.petblog.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/list")
    public Result getArticleList(@RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "10") int size,
                                @RequestParam(required = false) Long categoryId,
                                @RequestParam(required = false) String keyword) {
        IPage<Article> articlePage = articleService.getArticleList(page, size, categoryId, keyword);
        return Result.success(articlePage);
    }

    @GetMapping("/detail/{id}")
    public Result getArticleDetail(@PathVariable Long id, HttpServletRequest request) {
        Article article = articleService.getArticleDetail(id);
        // 如果用户已登录，返回是否已点赞
        Long userId = (Long) request.getAttribute("userId");
        boolean liked = userId != null && articleService.isLiked(userId, id);
        Map<String, Object> data = new HashMap<>();
        data.put("article", article);
        data.put("liked", liked);
        return Result.success(data);
    }

    @PostMapping
    public Result createArticle(HttpServletRequest request, @RequestBody Article article) {
        Long userId = (Long) request.getAttribute("userId");
        articleService.createArticle(userId, article);
        return Result.success("发布成功");
    }

    @PutMapping
    public Result updateArticle(HttpServletRequest request, @RequestBody Article article) {
        Long userId = (Long) request.getAttribute("userId");
        articleService.updateArticle(userId, article);
        return Result.success("更新成功");
    }

    @DeleteMapping("/{id}")
    public Result deleteArticle(HttpServletRequest request, @PathVariable Long id) {
        Long userId = (Long) request.getAttribute("userId");
        articleService.deleteArticle(userId, id);
        return Result.success("删除成功");
    }

    @PostMapping("/like/{id}")
    public Result toggleLike(HttpServletRequest request, @PathVariable Long id) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        boolean liked = articleService.toggleLike(userId, id);
        // 获取最新点赞数
        Article article = articleService.getById(id);
        Map<String, Object> data = new HashMap<>();
        data.put("liked", liked);
        data.put("likeCount", article.getLikeCount());
        return Result.success(liked ? "点赞成功" : "取消点赞成功", data);
    }

    @GetMapping("/my")
    public Result getMyArticles(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        List<Article> articles = articleService.getMyArticles(userId);
        return Result.success(articles);
    }
}