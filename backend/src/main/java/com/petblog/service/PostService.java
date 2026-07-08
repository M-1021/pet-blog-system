package com.petblog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.petblog.entity.Post;
import com.petblog.entity.Reply;

import java.util.List;

/**
 * 帖子服务接口（社区论坛功能）
 * 提供帖子的增删改查和回复功能
 */
public interface PostService extends IService<Post> {

    /**
     * 获取帖子列表（分页）
     * @param page 页码
     * @param size 每页数量
     * @param keyword 搜索关键词（可选）
     * @return 分页帖子列表
     */
    IPage<Post> getPostList(int page, int size, String keyword);

    /**
     * 获取帖子详情
     * @param id 帖子ID
     * @return 帖子详情
     */
    Post getPostDetail(Long id);

    /**
     * 创建帖子
     * @param userId 用户ID
     * @param post 帖子内容
     */
    void createPost(Long userId, Post post);

    /**
     * 删除帖子
     * @param userId 用户ID（用于权限验证）
     * @param postId 帖子ID
     */
    void deletePost(Long userId, Long postId);

    /**
     * 获取帖子的回复列表
     * @param postId 帖子ID
     * @return 回复列表
     */
    List<Reply> getReplies(Long postId);

    /**
     * 添加回复
     * @param userId 用户ID
     * @param reply 回复内容
     */
    void addReply(Long userId, Reply reply);
}
