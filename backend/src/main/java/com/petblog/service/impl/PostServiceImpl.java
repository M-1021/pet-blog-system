package com.petblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.petblog.entity.Post;
import com.petblog.entity.Reply;
import com.petblog.entity.User;
import com.petblog.mapper.PostMapper;
import com.petblog.service.PostService;
import com.petblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 帖子服务实现类（社区论坛功能）
 * 处理帖子的增删改查和回复业务逻辑
 */
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

    @Autowired
    private UserService userService;

    /**
     * 获取帖子列表（分页）
     * 支持按关键词搜索标题
     */
    @Override
    public IPage<Post> getPostList(int page, int size, String keyword) {
        Page<Post> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<>();

        // 按关键词搜索标题
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(Post::getTitle, keyword);
        }

        // 按创建时间降序排序
        wrapper.orderByDesc(Post::getCreateTime);

        IPage<Post> postPage = this.page(pageParam, wrapper);

        // 填充用户名
        postPage.getRecords().forEach(this::fillUserName);

        return postPage;
    }

    /**
     * 获取帖子详情
     */
    @Override
    public Post getPostDetail(Long id) {
        Post post = this.getById(id);
        if (post == null) {
            throw new RuntimeException("帖子不存在");
        }
        // 填充用户名
        fillUserName(post);
        return post;
    }

    /**
     * 创建帖子
     */
    @Override
    public void createPost(Long userId, Post post) {
        post.setUserId(userId);
        post.setCreateTime(LocalDateTime.now());
        this.save(post);
    }

    /**
     * 删除帖子
     * 只有帖子作者可以删除
     */
    @Override
    public void deletePost(Long userId, Long postId) {
        Post post = this.getById(postId);
        if (post == null) {
            throw new RuntimeException("帖子不存在");
        }
        // 验证是否为帖子作者
        if (!post.getUserId().equals(userId)) {
            throw new RuntimeException("无权删除此帖子");
        }
        this.removeById(postId);
    }

    /**
     * 获取帖子的回复列表
     */
    @Override
    public List<Reply> getReplies(Long postId) {
        // 这里简化处理，实际应该有ReplyService
        // 暂时返回空列表，需要配合ReplyMapper实现
        return List.of();
    }

    /**
     * 添加回复
     */
    @Override
    public void addReply(Long userId, Reply reply) {
        // 这里简化处理，实际应该有ReplyService
        // 需要配合ReplyMapper实现
    }

    /**
     * 填充用户名
     */
    private void fillUserName(Post post) {
        if (post.getUserId() != null) {
            User user = userService.getById(post.getUserId());
            if (user != null) {
                post.setUserName(user.getNickname() != null ? user.getNickname() : user.getUsername());
            }
        }
    }
}
