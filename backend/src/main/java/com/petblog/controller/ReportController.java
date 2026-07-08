package com.petblog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.petblog.entity.Report;
import com.petblog.service.ReportService;
import com.petblog.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 举报控制器
 * 处理举报相关的HTTP请求
 */
@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 创建举报（需要登录）
     * @param request HTTP请求
     * @param report 举报信息
     * @return 创建结果
     */
    @PostMapping
    public Result createReport(HttpServletRequest request, @RequestBody Report report) {
        Long userId = (Long) request.getAttribute("userId");
        reportService.createReport(userId, report);
        return Result.success("举报成功");
    }

    /**
     * 获取举报列表（管理员功能）
     * @param page 页码
     * @param size 每页数量
     * @param status 举报状态（可选）
     * @return 分页举报列表
     */
    @GetMapping("/list")
    public Result getReportList(@RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(required = false) Integer status) {
        IPage<Report> reportPage = reportService.getReportList(page, size, status);
        return Result.success(reportPage);
    }

    /**
     * 处理举报（管理员功能）
     * @param id 举报ID
     * @param action 处理动作：1=驳回，2=删除文章
     * @return 处理结果
     */
    @PutMapping("/handle/{id}")
    public Result handleReport(@PathVariable Long id,
                              @RequestParam Integer action) {
        reportService.handleReport(id, action);
        return Result.success("处理成功");
    }
}
