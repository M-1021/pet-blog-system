package com.petblog.controller;

import com.petblog.entity.Category;
import com.petblog.service.CategoryService;
import com.petblog.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类控制器
 * 处理分类相关的HTTP请求
 */
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 获取分类列表（公开接口）
     * @return 分类列表
     */
    @GetMapping("/list")
    public Result getCategoryList() {
        List<Category> categories = categoryService.getCategoryList();
        return Result.success(categories);
    }

    /**
     * 创建分类（管理员功能）
     * @param category 分类信息
     * @return 创建结果
     */
    @PostMapping
    public Result createCategory(@RequestBody Category category) {
        categoryService.createCategory(category);
        return Result.success("创建成功");
    }

    /**
     * 更新分类（管理员功能）
     * @param category 分类信息
     * @return 更新结果
     */
    @PutMapping
    public Result updateCategory(@RequestBody Category category) {
        categoryService.updateCategory(category);
        return Result.success("更新成功");
    }

    /**
     * 删除分类（管理员功能）
     * @param id 分类ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return Result.success("删除成功");
    }
}
