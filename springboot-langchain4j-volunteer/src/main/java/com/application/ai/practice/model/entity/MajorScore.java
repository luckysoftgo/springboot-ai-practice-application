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
@TableName("major_score")
public class MajorScore  implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long collegeId;
    private String majorName;
    private String majorCategory;
    private String subjectRequire;
    private Integer admissionYear;
    private Integer minScore;
    private Integer minRank;
    private Integer planNum;
    private Integer tuition;
    private Integer deleted;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
