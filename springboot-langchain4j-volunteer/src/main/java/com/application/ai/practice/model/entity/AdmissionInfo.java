package com.application.ai.practice.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("admission_info")
public class AdmissionInfo implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer collegeId;
    private Integer majorId;
    private Integer admissionYear;
    private String province;
    private Integer enrollmentPlan;
    private Integer maxScore;
    private Integer minScore;
    private BigDecimal avgScore;
    private Integer ranking;
    private Integer deleted;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}