package com.application.ai.practice.mapper;

import com.application.ai.practice.model.basic.PageQuery;
import com.application.ai.practice.model.entity.KnowledgeDoc;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface KnowledgeDocMapper extends BaseMapper<KnowledgeDoc> {

    /**
     * 分页查询知识库VO
     */
    IPage<KnowledgeDoc> selectKnowledgePage(Page<KnowledgeDoc> page, @Param("query") PageQuery query);

    /**
     * 根据标题批量删除（覆盖导入用）
     */
    void deleteByTitleList(@Param("titleList") List<String> titleList);

}