package com.application.ai.practice.tools;

import com.application.ai.practice.model.entity.AdmissionInfo;
import com.application.ai.practice.model.entity.CollegeInfo;
import com.application.ai.practice.model.entity.MajorScore;
import com.application.ai.practice.service.AdmissionInfoService;
import com.application.ai.practice.service.CollegeInfoService;
import com.application.ai.practice.service.MajorScoreService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import dev.langchain4j.agent.tool.Tool;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class VolunteerQueryTool {

    @Resource
    private CollegeInfoService collegeInfoService;

    @Resource
    private MajorScoreService majorScoreService;

    @Resource
    private AdmissionInfoService admissionInfoService;

    /**
     * AI工具：根据分数、省份查询可报考院校分数线
     */
    @Tool("根据考生分数、省份、年份查询对应院校专业历年最低录取位次和分数线")
    public List<MajorScore> queryScoreLine(Integer score, String province, Integer year) {
        return majorScoreService.lambdaQuery()
                .eq(MajorScore::getAdmissionYear, year)
                .le(MajorScore::getMinScore, score)
                .list();
    }

    /**
     * AI工具：根据城市、院校类型筛选院校
     */
    @Tool("根据意向城市、院校类型（公办/985/211）筛选院校信息")
    public List<CollegeInfo> queryCollegeByCityType(String city, String schoolType) {
        return collegeInfoService.lambdaQuery()
                .eq(CollegeInfo::getCityName, city)
                .eq(CollegeInfo::getSchoolType, schoolType)
                .list();
    }

    @Tool("根据位次查询可能录取的院校列表，返回院校名称、专业、最低位次")
    public List<AdmissionInfo> queryAdmissionByRank(Integer rank, Integer year, String province) {
        log.info("调用工具 queryAdmissionByRank, rank={}, year={}, province={}", rank, year, province);
        // 实际业务：查询 admission_info 表，按 ranking 排序，返回接近位次的院校
        // 此处简化，仅示例
        return admissionInfoService.lambdaQuery()
                .eq(AdmissionInfo::getAdmissionYear, year)
                .eq(AdmissionInfo::getProvince, province)
                .le(AdmissionInfo::getRanking, rank + 1000) // 位次范围
                .orderByAsc(AdmissionInfo::getRanking)
                .list();
    }

    @Tool("根据分数查询可能录取的院校列表，返回院校名称、专业、平均分")
    public List<AdmissionInfo> queryAdmissionByScore(Integer score, Integer year, String province) {
        log.info("调用工具 queryAdmissionByScore, score={}, year={}, province={}", score, year, province);
        return admissionInfoService.lambdaQuery()
                .eq(AdmissionInfo::getAdmissionYear, year)
                .eq(AdmissionInfo::getProvince, province)
                .le(AdmissionInfo::getMinScore, score) // 最低分小于等于考生分数
                .orderByDesc(AdmissionInfo::getAvgScore)
                .list();
    }

    @Tool("根据院校名称获取详细信息")
    public CollegeInfo getCollegeInfo(String collegeName) {
        log.info("调用工具 getCollegeInfo, collegeName={}", collegeName);
        return collegeInfoService.lambdaQuery()
                .eq(CollegeInfo::getCollegeName, collegeName)
                .one();
    }

    @Tool("根据省份名称查询高校列表，返回院校名称、类型、所在城市")
    public String queryCollegesByProvince(String provinceName) {
        LambdaQueryWrapper<CollegeInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CollegeInfo::getProvinceName, provinceName);
        List<CollegeInfo> list = collegeInfoService.list(wrapper);

        return list.stream()
                .map(c -> String.format("%s (%s, %s, %s)",
                        c.getCollegeName(), c.getSchoolType(),
                        c.getNatureType(), c.getCityName()))
                .collect(Collectors.joining("\n"));
    }

}