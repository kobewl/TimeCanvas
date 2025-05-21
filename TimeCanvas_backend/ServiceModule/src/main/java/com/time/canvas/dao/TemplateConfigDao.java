package com.time.canvas.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.time.canvas.domain.Template;
import com.time.canvas.exception.BusinessException;
import com.time.canvas.service.TemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.time.canvas.util.ErrorCode.NOT_FIND_AIMODEL_ERROR;
import static com.time.canvas.util.ErrorCode.OPERATION_ERROR;

@Repository
@Slf4j
public class TemplateConfigDao {

    @Autowired
    private TemplateService diaryTemplateService;

    public Template getDiaryTemplate(int diaryTemplateId) {
        try {
            // 使用 mybatis plus  进行数据库操作
            QueryWrapper<Template> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", diaryTemplateId);
            Template template = diaryTemplateService.getOne(queryWrapper);
            if (template == null) {
                log.error("未找到对应的配置 {}", template);
                throw new BusinessException(NOT_FIND_AIMODEL_ERROR);
            }
            return template;
        } catch (BusinessException businessException) {
            log.error("获取配置失败：{}", businessException.getMessage());
            throw new BusinessException(OPERATION_ERROR);
        }
    }
} 