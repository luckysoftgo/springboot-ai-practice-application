package com.application.ai.practice.service;

import com.application.ai.practice.mapper.StudentInfoMapper;
import com.application.ai.practice.model.entity.StudentInfo;
import com.application.ai.practice.service.impl.BasicServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class StudentInfoService extends BasicServiceImpl<StudentInfoMapper, StudentInfo> {

}
