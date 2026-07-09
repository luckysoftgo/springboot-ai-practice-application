package com.application.ai.practice.model;

/**
 * Worker Agent 执行结果
 *
 * @param subTaskId          子任务ID
 * @param agentType          处理的Agent类型
 * @param content            处理结果内容
 * @param requiresEscalation 是否需要转人工处理
 */
public record WorkerResultInfo(
        Integer subTaskId,
        String agentType,
        String content,
        boolean requiresEscalation
) {}
