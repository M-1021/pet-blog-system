package com.petblog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.petblog.entity.Report;

/**
 * 举报服务接口
 * 提供举报的创建、查询和处理功能
 */
public interface ReportService extends IService<Report> {

    /**
     * 创建举报
     * @param userId 举报用户ID
     * @param report 举报信息
     */
    void createReport(Long userId, Report report);

    /**
     * 获取举报列表（分页）
     * @param page 页码
     * @param size 每页数量
     * @param status 举报状态（可选，0-待处理，1-已处理）
     * @return 分页举报列表
     */
    IPage<Report> getReportList(int page, int size, Integer status);

    /**
     * 处理举报
     * @param reportId 举报ID
     * @param action 处理动作：1=驳回举报，2=删除被举报文章并标记举报
     */
    void handleReport(Long reportId, Integer action);
}
