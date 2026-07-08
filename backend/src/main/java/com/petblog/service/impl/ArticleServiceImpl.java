package com.petblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.petblog.entity.Article;
import com.petblog.entity.Category;
import com.petblog.entity.User;
import com.petblog.mapper.ArticleMapper;
import com.petblog.service.ArticleService;
import com.petblog.service.CategoryService;
import com.petblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章服务实现类
 * 处理文章的增删改查、点赞等业务逻辑
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 获取文章列表（分页）
     * 支持按分类和关键词筛选
     */
    @Override
    public IPage<Article> getArticleList(int page, int size, Long categoryId, String keyword) {
        Page<Article> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();

        // 按分类筛选
        if (categoryId != null) {
            wrapper.eq(Article::getCategoryId, categoryId);
        }

        // 按关键词搜索标题
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(Article::getTitle, keyword);
        }

        // 按创建时间降序排序
        wrapper.orderByDesc(Article::getCreateTime);

        IPage<Article> articlePage = this.page(pageParam, wrapper);

        // 填充作者名和分类名
        articlePage.getRecords().forEach(this::fillAuthorAndCategory);

        return articlePage;
    }

    /**
     * 获取文章详情
     * 查询文章并填充作者名和分类名
     */
    @Override
    public Article getArticleDetail(Long id) {
        Article article = this.getById(id);
        if (article == null) {
            throw new RuntimeException("文章不存在");
        }
        // 填充作者名和分类名
        fillAuthorAndCategory(article);
        return article;
    }

    /**
     * 创建文章
     */
    @Override
    public void createArticle(Long userId, Article article) {
        article.setUserId(userId);
        article.setLikeCount(0);
        article.setViewCount(0);
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        this.save(article);
    }

    /**
     * 更新文章
     * 验证是否为文章作者
     */
    @Override
    public void updateArticle(Long userId, Article article) {
        Article existingArticle = this.getById(article.getId());
        if (existingArticle == null) {
            throw new RuntimeException("文章不存在");
        }
        // 验证是否为文章作者
        if (!existingArticle.getUserId().equals(userId)) {
            throw new RuntimeException("无权修改此文章");
        }

        // 只更新允许修改的字段
        existingArticle.setTitle(article.getTitle());
        existingArticle.setContent(article.getContent());
        existingArticle.setCategoryId(article.getCategoryId());
        existingArticle.setCoverImage(article.getCoverImage());
        existingArticle.setUpdateTime(LocalDateTime.now());

        this.updateById(existingArticle);
    }

    /**
     * 删除文章
     * 验证是否为文章作者或管理员
     */
    @Override
    public void deleteArticle(Long userId, Long articleId) {
        Article article = this.getById(articleId);
        if (article == null) {
            throw new RuntimeException("文章不存在");
        }

        // 验证权限：文章作者或管理员可以删除
        User user = userService.getById(userId);
        if (!article.getUserId().equals(userId) && user.getRole() != 1) {
            throw new RuntimeException("无权删除此文章");
        }

        this.removeById(articleId);
    }

    /**
     * 点赞/取消点赞文章
     * 使用简单的计数方式实现
     */
    @Override
    public boolean toggleLike(Long userId, Long articleId) {
        Article article = this.getById(articleId);
        if (article == null) {
            throw new RuntimeException("文章不存在");
        }

        // 简化实现：直接增加或减少点赞数
        // 实际项目中应该有单独的点赞记录表
        Integer currentLikes = article.getLikeCount() != null ? article.getLikeCount() : 0;

        // 这里简化处理，实际应该查询点赞记录表判断是否已点赞
        article.setLikeCount(currentLikes + 1);
        this.updateById(article);
        return true;
    }

    /**
     * 获取我的文章列表
     */
    @Override
    public List<Article> getMyArticles(Long userId) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getUserId, userId)
               .orderByDesc(Article::getCreateTime);
        List<Article> articles = this.list(wrapper);
        articles.forEach(this::fillAuthorAndCategory);
        return articles;
    }

    /**
     * 填充作者名和分类名
     * 因为Article表中只存了userId和categoryId，需要查询对应的名称
     */
    private void fillAuthorAndCategory(Article article) {
        // 填充作者名
        if (article.getUserId() != null) {
            User author = userService.getById(article.getUserId());
            if (author != null) {
                article.setAuthorName(author.getNickname() != null ? author.getNickname() : author.getUsername());
            }
        }
        // 填充分类名
        if (article.getCategoryId() != null) {
            Category category = categoryService.getById(article.getCategoryId());
            if (category != null) {
                article.setCategoryName(category.getName());
            }
        }
    }
}
