package com.application.ai.practice.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("student_info")
public class StudentInfo  implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String studentName;
    private Integer studentScore;
    private Integer studentRank;
    private String subjectPhone;
    private String subjectCombination;
    private String targetCity;
    private String targetMajor;
    private Integer studentBudget;
    private String studentDesc;
    private Integer deleted;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}