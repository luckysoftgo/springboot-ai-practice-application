package com.application.ai.practice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgeImportResult {
    /**
     * 总条数
     */
    private Integer totalCount;
    /**
     * 成功条数
     */
    private Integer successCount;
    /**
     * 失败条数
     */
    private Integer failCount;
    /**
     * 失败信息列表
     */
    private List<String> failMsgList;
}
