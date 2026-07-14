package com.application.ai.practice.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.application.ai.practice.exception.BusinessException;
import com.application.ai.practice.mapper.KnowledgeDocMapper;
import com.application.ai.practice.model.basic.PageQuery;
import com.application.ai.practice.model.dto.KnowledgeBatchImport;
import com.application.ai.practice.model.dto.KnowledgeImportResult;
import com.application.ai.practice.model.entity.KnowledgeDoc;
import com.application.ai.practice.model.request.KnowledgeExcelModel;
import com.application.ai.practice.service.impl.BasicServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class KnowledgeDocService extends BasicServiceImpl<KnowledgeDocMapper, KnowledgeDoc> {

    @Autowired
    private KnowledgeDocMapper knowledgeDocMapper;

    public IPage<KnowledgeDoc> pageList(PageQuery pageQuery) {
        Page<KnowledgeDoc> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        return knowledgeDocMapper.selectKnowledgePage(page, pageQuery);
    }

    /**
     * 递归扫描目录，导入所有 PDF/MD 文件
     */
    public void importFromPath(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists() || !dir.isDirectory()) {
            throw new IllegalArgumentException("无效目录: " + dirPath);
        }
        // 递归遍历（使用 Files.walk 或递归）
        try (Stream<Path> paths = Files.walk(dir.toPath())) {
            paths.filter(Files::isRegularFile)
                    .filter(p -> {
                        String name = p.getFileName().toString().toLowerCase();
                        return name.endsWith(".pdf") || name.endsWith(".md");
                    })
                    .forEach(this::processFile);
        } catch (IOException e) {
            log.error("遍历目录失败", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 处理单个文件：读取内容，构建实体，保存到数据库（若已存在则更新）
     */
    private void processFile(Path filePath) {
        String fileName = filePath.getFileName().toString();
        String ext = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        String title = fileName; // 不含扩展名

        // 1. 读取内容
        String content = "";
        try {
            if ("pdf".equals(ext)) {
                content = extractPdfContent(filePath.toFile());
            } else if ("md".equals(ext)) {
                content = Files.readString(filePath, StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            log.error("读取文件失败: {}", fileName, e);
            return;
        }

        if (content.isBlank()) {
            log.warn("文件内容为空: {}", fileName);
            return;
        }

        // 2. 判断 doc_type
        String docType = inferDocType(title, content);

        // 3. 检查是否已存在（以标题+类型作为唯一键，或简单覆盖）
        KnowledgeDoc existing = this.lambdaQuery()
                .eq(KnowledgeDoc::getDocTitle, title)
                .eq(KnowledgeDoc::getDocType, docType)
                .eq(KnowledgeDoc::getDeleted, 0)
                .one();

        KnowledgeDoc doc = new KnowledgeDoc();
        doc.setDocTitle(title);
        doc.setDocType(docType);
        doc.setDocContent(content);
        doc.setDeleted(0);
        doc.setCreateTime(LocalDateTime.now());
        doc.setUpdateTime(LocalDateTime.now());

        if (existing != null) {
            doc.setId(existing.getId());
            this.updateById(doc);
            log.info("更新文档: {}", title);
        } else {
            this.save(doc);
            log.info("新增文档: {}", title);
        }
    }

    /**
     * 使用 PDFBox 提取 PDF 文本（支持中文）
     */
    private String extractPdfContent(File pdfFile) throws IOException {
        try (PDDocument document = Loader.loadPDF(pdfFile)) {
            // 若 PDF 有中文，推荐使用 PDFTextStripper 并设置编码
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setStartPage(1);
            stripper.setEndPage(document.getNumberOfPages());
            // 解决中文乱码：PDFBox 3.0 默认使用 UTF-8，但部分 PDF 需设置字符映射
            // 可自定义字符映射，但大多数中文 PDF 可直接提取
            return stripper.getText(document);
        }
    }

    /**
     * 根据文件名或内容关键字推断文档类型
     */
    private String inferDocType(String title, String content) {
        String lowerTitle = title.toLowerCase();
        String lowerContent = content.toLowerCase();

        if (lowerTitle.contains("专业") || lowerTitle.contains("天坑") || lowerTitle.contains("热门")) {
            return "专业介绍";
        } else if (lowerTitle.contains("大学") || lowerTitle.contains("学院") || lowerTitle.contains("招生")) {
            // 若内容含“院校”、“学校”等也可
            if (lowerContent.contains("院校") || lowerContent.contains("学校") || lowerContent.contains("办学")) {
                return "院校简章";
            }
            return "院校简章";
        } else if (lowerTitle.contains("政策") || lowerTitle.contains("规则") || lowerTitle.contains("填报")) {
            return "填报规则";
        } else {
            return "其他"; // 可根据需要调整
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public KnowledgeImportResult batchImport(KnowledgeBatchImport dto) {
        MultipartFile file = dto.getFile();
        KnowledgeImportResult result = new KnowledgeImportResult();
        List<String> failMsgList = new ArrayList<>();
        List<KnowledgeDoc> saveList = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream()) {
            // 读取Excel
            EasyExcel.read(inputStream, KnowledgeExcelModel.class, new AnalysisEventListener<KnowledgeExcelModel>() {
                @Override
                public void invoke(KnowledgeExcelModel excelModel, AnalysisContext context) {
                    Integer rowNum = context.readRowHolder().getRowIndex() + 1;
                    // 数据校验
                    if (excelModel.getTitle() == null || excelModel.getTitle().trim().isEmpty()) {
                        failMsgList.add("第" + rowNum + "行：标题不能为空");
                        return;
                    }
                    // 转换数据库实体
                    KnowledgeDoc entity = convertExcelToEntity(excelModel);
                    saveList.add(entity);
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                    // 读取完成后批量入库
                    int total = saveList.size() + failMsgList.size();
                    result.setTotalCount(total);
                    result.setFailCount(failMsgList.size());
                    result.setFailMsgList(failMsgList);
                    result.setSuccessCount(saveList.size());
                    if (!saveList.isEmpty()) {
                        // 判断是否覆盖重复标题
                        if (dto.getCoverRepeat() == 1) {
                            // 先删除重复标题再插入
                            List<String> titleList = saveList.stream()
                                    .map(KnowledgeDoc::getDocTitle)
                                    .collect(Collectors.toList());
                            knowledgeDocMapper.deleteByTitleList(titleList);
                        }
                        // 批量插入
                        saveBatch(saveList);
                    }
                }
            }).sheet().doRead();
        } catch (Exception e) {
            throw new BusinessException("文件解析失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * Excel模型转数据库实体
     */
    private KnowledgeDoc convertExcelToEntity(KnowledgeExcelModel excel) {
        KnowledgeDoc entity = new KnowledgeDoc();
        entity.setDocTitle(excel.getTitle());
        entity.setDocContent(excel.getContent());
        entity.setDocType(excel.getTags());
        entity.setCreateTime(LocalDateTime.now());
        return entity;
    }

}
