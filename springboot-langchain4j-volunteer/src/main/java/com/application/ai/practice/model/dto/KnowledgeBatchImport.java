package com.application.ai.practice.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class KnowledgeBatchImport {

    /**
     * Excel文件
     */
    @NotNull(message = "导入文件不能为空")
    private MultipartFile file;

    /**
     * 分类ID（可选）
     */
    private Long categoryId;

    /**
     * 是否覆盖重复标题 0否 1是
     */
    private Integer coverRepeat = 0;
}
