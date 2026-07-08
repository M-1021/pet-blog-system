package com.petblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.petblog.entity.Article;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文章表 Mapper - 继承 MyBatis-Plus 的 BaseMapper
 * BaseMapper 已内置了常用的 CRUD 方法，无需手写 SQL
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

}
