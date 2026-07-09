package com.application.ai.practice.model;

import java.util.List;

/**
 * 调度Agent输出：任务拆解结果
 */
public record TaskPlanInfo(
        String originalRequest,
        List<SubTask> subTasks) {

    /**
     * 子任务定义
     *
     * @param id            子任务序号
     * @param description   子任务描述
     * @param assignedAgent 分配给的Agent类型：TECH_SUPPORT / PRODUCT / SALES
     * @param keywords      相关关键词
     */
    public record SubTask(
            int id,
            String description,
            String assignedAgent,
            List<String> keywords
    ) {}
}
