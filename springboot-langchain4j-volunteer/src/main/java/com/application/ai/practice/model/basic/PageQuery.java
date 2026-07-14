package com.application.ai.practice.model.basic;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class PageQuery {

    /**
     * 当前页码，默认1
     */
    @Min(value = 1, message = "页码不能小于1")
    private Long pageNum = 1L;

    /**
     * 每页条数，默认10
     */
    @Min(value = 1, message = "每页条数不能小于1")
    private Long pageSize = 10L;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序方式 asc/desc
     */
    private String sortOrder;

}
