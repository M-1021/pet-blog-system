package com.petblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.petblog.entity.Category;
import com.petblog.mapper.CategoryMapper;
import com.petblog.service.CategoryService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 分类服务实现类
 * 处理分类的增删改查业务逻辑
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    /**
     * 获取所有分类列表
     * 按排序字段升序排列
     */
    @Override
    public List<Category> getCategoryList() {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Category::getSortOrder);
        return this.list(wrapper);
    }

    /**
     * 创建分类
     */
    @Override
    public void createCategory(Category category) {
        category.setCreateTime(LocalDateTime.now());
        this.save(category);
    }

    /**
     * 更新分类
     */
    @Override
    public void updateCategory(Category category) {
        this.updateById(category);
    }

    /**
     * 删除分类
     */
    @Override
    public void deleteCategory(Long id) {
        if (!this.removeById(id)) {
            throw new RuntimeException("删除失败，分类不存在");
        }
    }
}
