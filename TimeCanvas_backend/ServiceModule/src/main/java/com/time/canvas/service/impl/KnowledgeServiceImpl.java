package com.time.canvas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.time.canvas.assistant.KnowledgeAssistant;
import com.time.canvas.domain.Diary;
import com.time.canvas.domain.KnowledgeEntry;
import com.time.canvas.dto.knowledge.KnowledgeEntryAddRequest;
import com.time.canvas.dto.knowledge.KnowledgeEntryQueryRequest;
import com.time.canvas.exception.BusinessException;
import com.time.canvas.mapper.KnowledgeEntryMapper;
import com.time.canvas.service.DiaryService;
import com.time.canvas.service.KnowledgeService;
import com.time.canvas.util.ErrorCode;
import com.time.canvas.vo.knowledge.KnowledgeEntryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 知识库服务实现类
 * 
 * @author wangliang
 */
@Service
@Slf4j
public class KnowledgeServiceImpl extends ServiceImpl<KnowledgeEntryMapper, KnowledgeEntry> 
        implements KnowledgeService {

    @Autowired
    private KnowledgeEntryMapper knowledgeEntryMapper;

    @Autowired
    private KnowledgeAssistant knowledgeAssistant;

    @Autowired
    private DiaryService diaryService;

    @Override
    @Transactional
    public Long createKnowledgeEntry(KnowledgeEntryAddRequest addRequest, Long userId) {
        // 参数校验
        if (addRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数不能为空");
        }
        if (userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户ID无效");
        }

        KnowledgeEntry knowledgeEntry = new KnowledgeEntry();
        BeanUtils.copyProperties(addRequest, knowledgeEntry);
        knowledgeEntry.setUserId(userId);
        knowledgeEntry.setViewCount(0);
        knowledgeEntry.setCreatedTime(LocalDateTime.now());
        knowledgeEntry.setUpdatedTime(LocalDateTime.now());

        // 处理标签
        if (addRequest.getTags() != null && !addRequest.getTags().isEmpty()) {
            String tagsStr = String.join(",", addRequest.getTags());
            knowledgeEntry.setTags(tagsStr);
        }

        // 使用AI生成摘要
        if (knowledgeAssistant != null && StringUtils.hasText(addRequest.getContent())) {
            try {
                String summary = knowledgeAssistant.generateSummary(addRequest.getContent());
                knowledgeEntry.setSummary(summary);
            } catch (Exception e) {
                log.warn("AI生成摘要失败: {}", e.getMessage());
                // 摘要生成失败不影响创建
            }
        }

        // 如果没有提供标签，使用AI提取
        if (!StringUtils.hasText(knowledgeEntry.getTags()) && knowledgeAssistant != null) {
            try {
                String aiTags = knowledgeAssistant.extractTags(addRequest.getContent());
                knowledgeEntry.setTags(aiTags);
            } catch (Exception e) {
                log.warn("AI提取标签失败: {}", e.getMessage());
            }
        }

        boolean result = this.save(knowledgeEntry);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "创建知识条目失败");
        }

        return knowledgeEntry.getId();
    }

    @Override
    @Transactional
    public boolean updateKnowledgeEntry(Long id, KnowledgeEntryAddRequest addRequest, Long userId) {
        // 参数校验
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "知识条目ID无效");
        }
        if (addRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数不能为空");
        }

        KnowledgeEntry existingEntry = this.getById(id);
        if (existingEntry == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "知识条目不存在");
        }
        
        // 权限校验
        if (!existingEntry.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限修改该知识条目");
        }

        // 更新字段
        existingEntry.setTitle(addRequest.getTitle());
        existingEntry.setContent(addRequest.getContent());
        existingEntry.setCategoryId(addRequest.getCategoryId());
        existingEntry.setUpdatedTime(LocalDateTime.now());

        // 处理标签
        if (addRequest.getTags() != null && !addRequest.getTags().isEmpty()) {
            String tagsStr = String.join(",", addRequest.getTags());
            existingEntry.setTags(tagsStr);
        }

        // 重新生成摘要
        if (knowledgeAssistant != null && StringUtils.hasText(addRequest.getContent())) {
            try {
                String summary = knowledgeAssistant.generateSummary(addRequest.getContent());
                existingEntry.setSummary(summary);
            } catch (Exception e) {
                log.warn("AI生成摘要失败: {}", e.getMessage());
            }
        }

        return this.updateById(existingEntry);
    }

    @Override
    @Transactional
    public boolean deleteKnowledgeEntry(Long id, Long userId) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "知识条目ID无效");
        }

        KnowledgeEntry existingEntry = this.getById(id);
        if (existingEntry == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "知识条目不存在");
        }

        // 权限校验
        if (!existingEntry.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限删除该知识条目");
        }

        return this.removeById(id);
    }

    @Override
    public KnowledgeEntryVO getKnowledgeEntryVO(Long id, Long userId) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "知识条目ID无效");
        }

        KnowledgeEntry knowledgeEntry = this.getById(id);
        if (knowledgeEntry == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "知识条目不存在");
        }

        // 权限校验
        if (!knowledgeEntry.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限查看该知识条目");
        }

        // 增加查看次数
        knowledgeEntryMapper.incrementViewCount(id);

        return convertToVO(knowledgeEntry);
    }

    @Override
    public IPage<KnowledgeEntryVO> listKnowledgeEntryVO(KnowledgeEntryQueryRequest queryRequest) {
        // 创建分页对象
        Page<KnowledgeEntry> page = new Page<>(queryRequest.getCurrent(), queryRequest.getPageSize());

        // 查询数据
        IPage<KnowledgeEntry> entryPage = knowledgeEntryMapper.selectKnowledgeEntryPage(page, queryRequest);

        // 转换为VO
        Page<KnowledgeEntryVO> voPage = new Page<>(page.getCurrent(), page.getSize(), entryPage.getTotal());
        List<KnowledgeEntryVO> voList = entryPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);

        return voPage;
    }

    @Override
    public List<KnowledgeEntryVO> searchKnowledgeEntries(KnowledgeEntryQueryRequest queryRequest) {
        if (queryRequest == null || queryRequest.getUserId() == null) {
            return new ArrayList<>();
        }

        List<KnowledgeEntry> entries;

        // 如果启用语义搜索
        if (Boolean.TRUE.equals(queryRequest.getEnableSemanticSearch()) 
            && StringUtils.hasText(queryRequest.getSemanticQuery())) {
            // TODO: 实现向量搜索逻辑
            log.info("语义搜索功能待实现，当前使用关键词搜索");
            entries = knowledgeEntryMapper.searchKnowledgeEntries(
                queryRequest.getSemanticQuery(), 
                queryRequest.getUserId(), 
                20
            );
        } else if (StringUtils.hasText(queryRequest.getKeyword())) {
            // 关键词搜索
            entries = knowledgeEntryMapper.searchKnowledgeEntries(
                queryRequest.getKeyword(), 
                queryRequest.getUserId(), 
                20
            );
        } else {
            // 标签搜索
            if (queryRequest.getTags() != null && !queryRequest.getTags().isEmpty()) {
                entries = knowledgeEntryMapper.selectByTags(queryRequest.getTags(), queryRequest.getUserId());
            } else {
                entries = new ArrayList<>();
            }
        }

        return entries.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<KnowledgeEntryVO> getRelatedEntries(Long entryId, Long userId, Integer limit) {
        if (entryId == null || userId == null) {
            return new ArrayList<>();
        }

        // 获取当前知识条目
        KnowledgeEntry currentEntry = this.getById(entryId);
        if (currentEntry == null || !currentEntry.getUserId().equals(userId)) {
            return new ArrayList<>();
        }

        // 基于标签查找相关条目
        List<KnowledgeEntry> relatedEntries = new ArrayList<>();
        if (StringUtils.hasText(currentEntry.getTags())) {
            List<String> tags = Arrays.asList(currentEntry.getTags().split(","));
            relatedEntries = knowledgeEntryMapper.selectByTags(tags, userId);
            
            // 移除当前条目
            relatedEntries = relatedEntries.stream()
                    .filter(entry -> !entry.getId().equals(entryId))
                    .limit(limit != null ? limit : 5)
                    .collect(Collectors.toList());
        }

        return relatedEntries.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<KnowledgeEntryVO> extractFromDiary(Long diaryId, Long userId) {
        if (diaryId == null || userId == null) {
            return new ArrayList<>();
        }

        // 获取日记内容
        Diary diary = diaryService.getById(diaryId);
        if (diary == null || !diary.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "日记不存在或无权限访问");
        }

        List<KnowledgeEntryVO> extractedEntries = new ArrayList<>();

        if (knowledgeAssistant != null && StringUtils.hasText(diary.getContent())) {
            try {
                // 使用AI从日记中提取知识点
                String extractedJson = knowledgeAssistant.extractKnowledgeFromDiary(diary.getContent());
                
                // TODO: 解析JSON并创建知识条目
                log.info("从日记 {} 提取的知识点: {}", diaryId, extractedJson);
                
                // 这里简化处理，直接创建一个知识条目
                KnowledgeEntryAddRequest addRequest = new KnowledgeEntryAddRequest();
                addRequest.setTitle("来自日记：" + (StringUtils.hasText(diary.getTitle()) ? diary.getTitle() : "无标题"));
                addRequest.setContent(diary.getContent());
                addRequest.setSourceType("diary_extract");
                addRequest.setSourceId(diaryId);

                Long entryId = createKnowledgeEntry(addRequest, userId);
                KnowledgeEntryVO entryVO = getKnowledgeEntryVO(entryId, userId);
                extractedEntries.add(entryVO);

            } catch (Exception e) {
                log.error("从日记提取知识点失败: {}", e.getMessage(), e);
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "提取知识点失败");
            }
        }

        return extractedEntries;
    }

    @Override
    public String intelligentQA(String question, Long userId) {
        if (!StringUtils.hasText(question) || userId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "问题内容或用户ID不能为空");
        }

        if (knowledgeAssistant == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "AI助手未配置");
        }

        try {
            // 搜索相关知识条目
            KnowledgeEntryQueryRequest queryRequest = new KnowledgeEntryQueryRequest();
            queryRequest.setKeyword(question);
            queryRequest.setUserId(userId);
            
            List<KnowledgeEntryVO> relatedEntries = searchKnowledgeEntries(queryRequest);
            
            // 构建知识上下文
            StringBuilder knowledgeContext = new StringBuilder();
            for (KnowledgeEntryVO entry : relatedEntries) {
                knowledgeContext.append("标题: ").append(entry.getTitle()).append("\n");
                knowledgeContext.append("内容: ").append(entry.getContent()).append("\n\n");
            }

            // 使用AI回答问题
            return knowledgeAssistant.answerQuestion(question, knowledgeContext.toString());

        } catch (Exception e) {
            log.error("智能问答失败: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "智能问答失败");
        }
    }

    @Override
    public List<String> getPopularTags(Long userId, Integer limit) {
        if (userId == null) {
            return new ArrayList<>();
        }
        
        try {
            return knowledgeEntryMapper.getPopularTags(userId, limit != null ? limit : 10);
        } catch (Exception e) {
            log.error("获取热门标签失败: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public String generateSummary(String content) {
        if (!StringUtils.hasText(content)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容不能为空");
        }

        if (knowledgeAssistant == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "AI助手未配置");
        }

        try {
            return knowledgeAssistant.generateSummary(content);
        } catch (Exception e) {
            log.error("生成摘要失败: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "生成摘要失败");
        }
    }

    @Override
    @Transactional
    public Integer batchImport(List<KnowledgeEntryAddRequest> entries, Long userId) {
        if (entries == null || entries.isEmpty() || userId == null) {
            return 0;
        }

        int successCount = 0;
        for (KnowledgeEntryAddRequest entry : entries) {
            try {
                createKnowledgeEntry(entry, userId);
                successCount++;
            } catch (Exception e) {
                log.error("批量导入知识条目失败: {}", e.getMessage(), e);
                // 继续处理下一个条目
            }
        }

        return successCount;
    }

    /**
     * 将实体转换为VO
     */
    private KnowledgeEntryVO convertToVO(KnowledgeEntry knowledgeEntry) {
        if (knowledgeEntry == null) {
            return null;
        }

        KnowledgeEntryVO vo = new KnowledgeEntryVO();
        BeanUtils.copyProperties(knowledgeEntry, vo);

        // 处理标签
        if (StringUtils.hasText(knowledgeEntry.getTags())) {
            vo.setTagList(Arrays.asList(knowledgeEntry.getTags().split(",")));
        }

        return vo;
    }
}