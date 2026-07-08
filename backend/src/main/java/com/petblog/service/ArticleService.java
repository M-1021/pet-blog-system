package com.petblog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.petblog.entity.Article;

import java.util.List;

/**
 * 文章服务接口
 * 提供文章的增删改查、点赞等功能
 */
public interface ArticleService extends IService<Article> {

    /**
     * 获取文章列表（分页）
     * @param page 页码
     * @param size 每页数量
     * @param categoryId 分类ID（可选）
     * @param keyword 搜索关键词（可选）
     * @return 分页文章列表
     */
    IPage<Article> getArticleList(int page, int size, Long categoryId, String keyword);

    /**
     * 获取文章详情
     * @param id 文章ID
     * @return 文章详情（包含作者名和分类名）
     */
    Article getArticleDetail(Long id);

    /**
     * 创建文章
     * @param userId 作者ID
     * @param article 文章信息
     */
    void createArticle(Long userId, Article article);

    /**
     * 更新文章
     * @param userId 用户ID（用于权限验证）
     * @param article 文章信息
     */
    void updateArticle(Long userId, Article article);

    /**
     * 删除文章
     * @param userId 用户ID（用于权限验证）
     * @param articleId 文章ID
     */
    void deleteArticle(Long userId, Long articleId);

    /**
     * 点赞/取消点赞文章
     * @param userId 用户ID
     * @param articleId 文章ID
     * @return true表示已点赞，false表示已取消点赞
     */
    boolean toggleLike(Long userId, Long articleId);

    /**
     * 获取我的文章列表
     * @param userId 用户ID
     * @return 文章列表
     */
    List<Article> getMyArticles(Long userId);
}
