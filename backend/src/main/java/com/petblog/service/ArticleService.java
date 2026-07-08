package com.petblog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.petblog.entity.Article;

import java.util.List;

public interface ArticleService extends IService<Article> {

    IPage<Article> getArticleList(int page, int size, Long categoryId, String keyword);

    Article getArticleDetail(Long id);

    void createArticle(Long userId, Article article);

    void updateArticle(Long userId, Article article);

    void deleteArticle(Long userId, Long articleId);

    boolean toggleLike(Long userId, Long articleId);

    boolean isLiked(Long userId, Long articleId);

    List<Article> getMyArticles(Long userId);
}