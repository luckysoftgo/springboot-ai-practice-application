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
@TableName("college_info")
public class CollegeInfo  implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String collegeName;
    private String provinceName;
    private String cityName;
    private String schoolType;
    private String natureType;
    private Integer avgTuition;
    private String collegeDesc;
    private Integer deleted;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}