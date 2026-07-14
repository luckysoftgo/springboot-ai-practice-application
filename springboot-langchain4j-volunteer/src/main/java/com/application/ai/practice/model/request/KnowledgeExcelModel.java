package com.application.ai.practice.model.request;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgeExcelModel {

    @ExcelProperty(value = "标题", index = 0)
    private String title;

    @ExcelProperty(value = "内容", index = 1)
    private String content;

    @ExcelProperty(value = "标签（逗号分隔）", index = 2)
    private String tags;
}
