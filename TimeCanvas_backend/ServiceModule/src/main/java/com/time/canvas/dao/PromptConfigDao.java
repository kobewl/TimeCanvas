package com.time.canvas.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.time.canvas.domain.AiConfig;
import com.time.canvas.domain.Prompt;
import com.time.canvas.exception.BusinessException;
import com.time.canvas.service.PromptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.SslBundleSslEngineFactory;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
            queryWrapper.eq("name", promptName);
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