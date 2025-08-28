package com.time.canvas.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.time.canvas.domain.Prompt;
import com.time.canvas.exception.BusinessException;
import com.time.canvas.service.PromptService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.time.canvas.util.ErrorCode.NOT_FIND_AIMODEL_ERROR;
import static com.time.canvas.util.ErrorCode.OPERATION_ERROR;

@Repository
@Slf4j
public class PromptConfigDao {

    @Autowired
    private PromptService promptService;

    public Prompt getConfig(String promptName) {
        try {
            // 使用 mybatis plus  进行数据库操作
            QueryWrapper<Prompt> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", "日记生成");
            Prompt prompt = promptService.getOne(queryWrapper);
            if (prompt == null) {
                log.error("未找到对应的配置 {}", promptName);
                throw new BusinessException(NOT_FIND_AIMODEL_ERROR);
            }
            return prompt;
        } catch (BusinessException businessException) {
            log.error("获取配置失败：{}", businessException.getMessage());
            throw new BusinessException(OPERATION_ERROR);
        }
    }
} 