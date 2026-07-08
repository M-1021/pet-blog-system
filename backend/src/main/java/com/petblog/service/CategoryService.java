package com.petblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.petblog.entity.Category;

import java.util.List;

/**
 * 分类服务接口
 * 提供分类的增删改查功能
 */
public interface CategoryService extends IService<Category> {

    /**
     * 获取所有分类列表
     * 按排序字段升序排列
     * @return 分类列表
     */
    List<Category> getCategoryList();

    /**
     * 创建分类
     * @param category 分类信息
     */
    void createCategory(Category category);

    /**
     * 更新分类
     * @param category 分类信息
     */
    void updateCategory(Category category);

    /**
     * 删除分类
     * @param id 分类ID
     */
    void deleteCategory(Long id);
}
