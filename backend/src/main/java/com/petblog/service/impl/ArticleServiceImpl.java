package com.petblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.petblog.entity.Article;
import com.petblog.entity.ArticleLike;
import com.petblog.entity.Category;
import com.petblog.entity.User;
import com.petblog.mapper.ArticleLikeMapper;
import com.petblog.mapper.ArticleMapper;
import com.petblog.service.ArticleService;
import com.petblog.service.CategoryService;
import com.petblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ArticleLikeMapper articleLikeMapper;

    @Override
    public IPage<Article> getArticleList(int page, int size, Long categoryId, String keyword) {
        Page<Article> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        if (categoryId != null) {
            wrapper.eq(Article::getCategoryId, categoryId);
        }
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(Article::getTitle, keyword);
        }
        wrapper.orderByDesc(Article::getCreateTime);
        IPage<Article> articlePage = this.page(pageParam, wrapper);
        articlePage.getRecords().forEach(this::fillAuthorAndCategory);
        return articlePage;
    }

    @Override
    public Article getArticleDetail(Long id) {
        Article article = this.getById(id);
        if (article == null) {
            throw new RuntimeException("文章不存在");
        }
        fillAuthorAndCategory(article);
        return article;
    }

    @Override
    public void createArticle(Long userId, Article article) {
        article.setUserId(userId);
        article.setLikeCount(0);
        article.setViewCount(0);
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        this.save(article);
    }

    @Override
    public void updateArticle(Long userId, Article article) {
        Article existingArticle = this.getById(article.getId());
        if (existingArticle == null) {
            throw new RuntimeException("文章不存在");
        }
        if (!existingArticle.getUserId().equals(userId)) {
            throw new RuntimeException("无权修改此文章");
        }
        existingArticle.setTitle(article.getTitle());
        existingArticle.setContent(article.getContent());
        existingArticle.setCategoryId(article.getCategoryId());
        existingArticle.setCoverImage(article.getCoverImage());
        existingArticle.setUpdateTime(LocalDateTime.now());
        this.updateById(existingArticle);
    }

    @Override
    public void deleteArticle(Long userId, Long articleId) {
        Article article = this.getById(articleId);
        if (article == null) {
            throw new RuntimeException("文章不存在");
        }
        User user = userService.getById(userId);
        if (!article.getUserId().equals(userId) && user.getRole() != 1) {
            throw new RuntimeException("无权删除此文章");
        }
        this.removeById(articleId);
    }

    @Override
    @Transactional
    public boolean toggleLike(Long userId, Long articleId) {
        Article article = this.getById(articleId);
        if (article == null) {
            throw new RuntimeException("文章不存在");
        }

        // 查询是否已点赞
        LambdaQueryWrapper<ArticleLike> likeWrapper = new LambdaQueryWrapper<>();
        likeWrapper.eq(ArticleLike::getUserId, userId)
                   .eq(ArticleLike::getArticleId, articleId);
        ArticleLike existingLike = articleLikeMapper.selectOne(likeWrapper);

        Integer currentLikes = article.getLikeCount() != null ? article.getLikeCount() : 0;

        if (existingLike != null) {
            // 已点赞 -> 取消点赞
            articleLikeMapper.deleteById(existingLike.getId());
            article.setLikeCount(Math.max(0, currentLikes - 1));
            this.updateById(article);
            return false;
        } else {
            // 未点赞 -> 点赞
            ArticleLike newLike = new ArticleLike();
            newLike.setUserId(userId);
            newLike.setArticleId(articleId);
            newLike.setCreateTime(LocalDateTime.now());
            articleLikeMapper.insert(newLike);
            article.setLikeCount(currentLikes + 1);
            this.updateById(article);
            return true;
        }
    }

    /**
     * 检查用户是否已点赞某文章
     */
    @Override
    public boolean isLiked(Long userId, Long articleId) {
        if (userId == null) return false;
        LambdaQueryWrapper<ArticleLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleLike::getUserId, userId)
               .eq(ArticleLike::getArticleId, articleId);
        return articleLikeMapper.selectCount(wrapper) > 0;
    }

    @Override
    public List<Article> getMyArticles(Long userId) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getUserId, userId)
               .orderByDesc(Article::getCreateTime);
        List<Article> articles = this.list(wrapper);
        articles.forEach(this::fillAuthorAndCategory);
        return articles;
    }

    private void fillAuthorAndCategory(Article article) {
        if (article.getUserId() != null) {
            User author = userService.getById(article.getUserId());
            if (author != null) {
                article.setAuthorName(author.getNickname() != null ? author.getNickname() : author.getUsername());
            }
        }
        if (article.getCategoryId() != null) {
            Category category = categoryService.getById(article.getCategoryId());
            if (category != null) {
                article.setCategoryName(category.getName());
            }
        }
    }
}