package com.petblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.petblog.entity.Post;
import org.apache.ibatis.annotations.Mapper;

/**
 * 帖子表 Mapper - 继承 MyBatis-Plus 的 BaseMapper
 * BaseMapper 已内置了常用的 CRUD 方法，无需手写 SQL
 */
@Mapper
public interface PostMapper extends BaseMapper<Post> {

}
