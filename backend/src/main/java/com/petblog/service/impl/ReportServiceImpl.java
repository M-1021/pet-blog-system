package com.petblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.petblog.entity.Report;
import com.petblog.mapper.ReportMapper;
import com.petblog.service.ArticleService;
import com.petblog.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 举报服务实现类
 * 处理举报的创建、查询和处理业务逻辑
 */
@Service
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements ReportService {

    @Autowired
    private ArticleService articleService;

    /**
     * 创建举报
     */
    @Override
    public void createReport(Long userId, Report report) {
        report.setUserId(userId);
        report.setStatus(0); // 0表示待处理
        report.setCreateTime(LocalDateTime.now());
        this.save(report);
    }

    /**
     * 获取举报列表（分页）
     * 支持按状态筛选
     */
    @Override
    public IPage<Report> getReportList(int page, int size, Integer status) {
        Page<Report> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Report> wrapper = new LambdaQueryWrapper<>();

        // 按状态筛选
        if (status != null) {
            wrapper.eq(Report::getStatus, status);
        }

        // 按创建时间降序排序
        wrapper.orderByDesc(Report::getCreateTime);

        return this.page(pageParam, wrapper);
    }

    /**
     * 处理举报
     * action: 1=驳回举报，2=删除被举报文章并标记举报
     */
    @Override
    @Transactional
    public void handleReport(Long reportId, Integer action) {
        Report report = this.getById(reportId);
        if (report == null) {
            throw new RuntimeException("举报不存在");
        }

        if (action == 1) {
            // 驳回举报，标记为已处理
            report.setStatus(1);
            report.setResult("举报已驳回");
            this.updateById(report);
        } else if (action == 2) {
            // 删除被举报的文章
            if (report.getArticleId() != null) {
                articleService.removeById(report.getArticleId());
            }
            // 标记举报为已处理
            report.setStatus(1);
            report.setResult("举报属实，文章已删除");
            this.updateById(report);
        } else {
            throw new RuntimeException("无效的处理动作");
        }
    }
}
