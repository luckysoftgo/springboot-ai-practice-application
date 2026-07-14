package com.application.ai.practice.controller;

import com.application.ai.practice.model.basic.BaseResult;
import com.application.ai.practice.model.entity.StudentInfo;
import com.application.ai.practice.service.StudentInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@Tag(name = "学生信息管理")
public class StudentInfoController {

    @Resource
    private StudentInfoService studentInfoService;

    @PostMapping("/add")
    @Operation(summary = "新增学生")
    public BaseResult<Boolean> add(@RequestBody StudentInfo studentInfo) {
        Boolean result = studentInfoService.saveEntity(studentInfo);
        return BaseResult.success(result);
    }

    @PutMapping("/update")
    @Operation(summary = "修改学生")
    public BaseResult<Boolean> update(@RequestBody StudentInfo studentInfo) {
        Boolean result = studentInfoService.updateEntity(studentInfo);
        return BaseResult.success(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除学生")
    public BaseResult<Boolean> delete(@PathVariable Long id) {
        Boolean result = studentInfoService.removeById(id);
        return BaseResult.success(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询")
    public BaseResult<StudentInfo> get(@PathVariable Long id) {
        StudentInfo result = studentInfoService.getById(id);
        return BaseResult.success(result);
    }

    @GetMapping("/list")
    @Operation(summary = "全量列表")
    public BaseResult<List<StudentInfo>> list() {
        List<StudentInfo> result = studentInfoService.list();
        return BaseResult.success(result);
    }
}