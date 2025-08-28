-- TimeCanvas 项目数据库建表脚本
-- 创建时间: 2025-08-27
-- 数据库: MySQL 8.0+
-- 字符集: utf8mb4

-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS `timecanvas_db` 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE `timecanvas_db`;

-- ========================================
-- 1. 用户表 (user)
-- ========================================
CREATE TABLE `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `user_account` VARCHAR(100) NOT NULL COMMENT '用户账号',
    `user_name` VARCHAR(100) NOT NULL COMMENT '用户名',
    `user_password` VARCHAR(255) NOT NULL COMMENT '用户密码',
    `user_role` INT DEFAULT 3 COMMENT '用户角色: 1-管理员, 2-VIP, 3-普通用户',
    `user_email` VARCHAR(255) DEFAULT NULL COMMENT '用户邮箱',
    `user_phone` VARCHAR(20) DEFAULT NULL COMMENT '用户电话',
    `avatar` VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
    `bio` TEXT DEFAULT NULL COMMENT '个人简介',
    `gender` ENUM('male', 'female', 'other') DEFAULT NULL COMMENT '性别: male-男, female-女, other-其他',
    `birth_date` DATE DEFAULT NULL COMMENT '出生日期',
    `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete` TINYINT DEFAULT 0 COMMENT '是否删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_account` (`user_account`),
    KEY `idx_user_email` (`user_email`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ========================================
-- 2. 日记模板表 (template)
-- ========================================
CREATE TABLE `template` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '模板ID',
    `type` VARCHAR(50) NOT NULL COMMENT '模版类型: 如日记、周报、月报',
    `content` TEXT NOT NULL COMMENT '模版内容',
    `creator_id` BIGINT DEFAULT NULL COMMENT '创建人ID，NULL表示系统创建',
    `tags` VARCHAR(500) DEFAULT NULL COMMENT '模版标签，逗号分隔，如日常,反思,情感',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete` TINYINT DEFAULT 0 COMMENT '是否删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_type` (`type`),
    KEY `idx_creator_id` (`creator_id`),
    KEY `idx_create_time` (`create_time`),
    CONSTRAINT `fk_template_creator` FOREIGN KEY (`creator_id`) REFERENCES `user` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='日记模版表';

-- ========================================
-- 3. 日记表 (diary)
-- ========================================
CREATE TABLE `diary` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日记ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `title` VARCHAR(200) DEFAULT NULL COMMENT '日记标题',
    `content` TEXT NOT NULL COMMENT '日记内容',
    `template_id` BIGINT DEFAULT NULL COMMENT '模版ID',
    `mood` VARCHAR(50) DEFAULT NULL COMMENT '心情',
    `tags` VARCHAR(500) DEFAULT NULL COMMENT '标签，逗号分隔',
    `visibility` ENUM('public', 'private') DEFAULT 'private' COMMENT '可见性: public-公开, private-私有',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete` TINYINT DEFAULT 0 COMMENT '是否删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_template_id` (`template_id`),
    KEY `idx_create_time` (`create_time`),
    KEY `idx_mood` (`mood`),
    CONSTRAINT `fk_diary_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_diary_template` FOREIGN KEY (`template_id`) REFERENCES `template` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='日记表';

-- ========================================
-- 4. 待办事项表 (todo_list)
-- ========================================
CREATE TABLE `todo_list` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '待办事项ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `content` VARCHAR(500) NOT NULL COMMENT '待办事项内容',
    `priority` INT DEFAULT 0 COMMENT '优先级: 0-普通, 1-高, 2-紧急',
    `status` ENUM('pending', 'completed') DEFAULT 'pending' COMMENT '状态: pending-待办, completed-已完成',
    `due_date` DATETIME DEFAULT NULL COMMENT '截止日期',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete` TINYINT DEFAULT 0 COMMENT '是否删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`),
    KEY `idx_priority` (`priority`),
    KEY `idx_due_date` (`due_date`),
    CONSTRAINT `fk_todo_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Todo List 表';

-- ========================================
-- 5. 财务支出表 (expense)
-- ========================================
CREATE TABLE `expense` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '支出记录ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `item` VARCHAR(200) NOT NULL COMMENT '物品',
    `amount` DECIMAL(10,2) NOT NULL COMMENT '金额',
    `type` ENUM('income', 'expense') NOT NULL COMMENT '类型: income-收入, expense-支出',
    `expense_date` DATE NOT NULL COMMENT '开支日期',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete` TINYINT DEFAULT 0 COMMENT '是否删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_type` (`type`),
    KEY `idx_expense_date` (`expense_date`),
    KEY `idx_create_time` (`create_time`),
    CONSTRAINT `fk_expense_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='开支表';

-- ========================================
-- 6. 报表表 (report)
-- ========================================
CREATE TABLE `report` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '报表ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `content` TEXT NOT NULL COMMENT '报告内容',
    `type` ENUM('daily', 'weekly', 'monthly') NOT NULL COMMENT '类型: daily-日报, weekly-周报, monthly-月报',
    `report_date` DATE NOT NULL COMMENT '报告日期',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete` TINYINT DEFAULT 0 COMMENT '是否删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_type` (`type`),
    KEY `idx_report_date` (`report_date`),
    CONSTRAINT `fk_report_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='日报、周报、月报表';

-- ========================================
-- 7. AI配置表 (ai_config)
-- ========================================
CREATE TABLE `ai_config` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'AI配置ID',
    `chat_model` VARCHAR(100) NOT NULL COMMENT '模型类型: 如 doubao-1-5-lite-32k-250115',
    `api_key` VARCHAR(500) NOT NULL COMMENT 'API密钥',
    `model_name` VARCHAR(100) NOT NULL COMMENT '模型名称',
    `base_url` VARCHAR(500) NOT NULL COMMENT 'API基础地址: 如 https://ark.cn-beijing.volces.com/api/v3',
    `log_requests` TINYINT DEFAULT 0 COMMENT '是否记录请求日志: 0-否, 1-是',
    `log_responses` TINYINT DEFAULT 0 COMMENT '是否记录响应日志: 0-否, 1-是',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete` TINYINT DEFAULT 0 COMMENT '是否删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_chat_model` (`chat_model`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI配置平台表';

-- ========================================
-- 8. 提示词表 (prompt)
-- ========================================
CREATE TABLE `prompt` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '提示词ID',
    `name` VARCHAR(100) NOT NULL COMMENT 'Prompt名称',
    `content` TEXT NOT NULL COMMENT 'Prompt内容',
    `type` ENUM('diary', 'report', 'todo', 'expense', 'analysis', 'knowledge', 'other') NOT NULL COMMENT '类型: diary-日记生成, report-报告生成, todo-待办生成, expense-开支分析, analysis-情感分析, knowledge-知识处理, other-其他',
    `creator_id` BIGINT DEFAULT NULL COMMENT '创建人ID，NULL表示系统默认',
    `is_system` TINYINT DEFAULT 0 COMMENT '是否系统默认: 0-用户自定义, 1-系统默认',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete` TINYINT DEFAULT 0 COMMENT '是否删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_type` (`type`),
    KEY `idx_creator_id` (`creator_id`),
    KEY `idx_is_system` (`is_system`),
    CONSTRAINT `fk_prompt_creator` FOREIGN KEY (`creator_id`) REFERENCES `user` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Prompt表';

-- ========================================
-- 9. 知识库分类表 (knowledge_category)
-- ========================================
CREATE TABLE `knowledge_category` (
    `id` BIGINT NOT NULL COMMENT '分类ID',
    `name` VARCHAR(100) NOT NULL COMMENT '分类名称',
    `description` TEXT DEFAULT NULL COMMENT '分类描述',
    `parent_id` BIGINT DEFAULT NULL COMMENT '父分类ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `sort_order` INT DEFAULT 0 COMMENT '排序顺序',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '是否删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_sort_order` (`sort_order`),
    UNIQUE KEY `uk_user_category_name` (`user_id`, `name`, `parent_id`),
    CONSTRAINT `fk_category_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_category_parent` FOREIGN KEY (`parent_id`) REFERENCES `knowledge_category` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='知识分类表';

-- ========================================
-- 10. 知识库条目表 (knowledge_entry)
-- ========================================
CREATE TABLE `knowledge_entry` (
    `id` BIGINT NOT NULL COMMENT '知识条目ID',
    `title` VARCHAR(200) NOT NULL COMMENT '标题',
    `content` TEXT NOT NULL COMMENT '内容',
    `summary` TEXT DEFAULT NULL COMMENT 'AI生成的摘要',
    `category_id` BIGINT DEFAULT NULL COMMENT '分类ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `tags` VARCHAR(500) DEFAULT NULL COMMENT '标签，逗号分隔',
    `view_count` INT DEFAULT 0 COMMENT '查看次数',
    `embedding_vector` JSON DEFAULT NULL COMMENT '向量嵌入（JSON格式存储）',
    `source_type` ENUM('manual', 'diary_extract', 'import') DEFAULT 'manual' COMMENT '来源类型：manual-手动创建，diary_extract-日记提取，import-导入',
    `source_id` BIGINT DEFAULT NULL COMMENT '来源ID（如日记ID）',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '是否删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_source_type` (`source_type`),
    KEY `idx_source_id` (`source_id`),
    KEY `idx_created_time` (`created_time`),
    FULLTEXT KEY `ft_title_content` (`title`, `content`),
    CONSTRAINT `fk_knowledge_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_knowledge_category` FOREIGN KEY (`category_id`) REFERENCES `knowledge_category` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='知识库条目表';

-- ========================================
-- 11. 知识关联表 (knowledge_relation)
-- ========================================
CREATE TABLE `knowledge_relation` (
    `id` BIGINT NOT NULL COMMENT '关联ID',
    `source_entry_id` BIGINT NOT NULL COMMENT '源知识条目ID',
    `target_entry_id` BIGINT NOT NULL COMMENT '目标知识条目ID',
    `relation_type` ENUM('related', 'reference', 'derived') DEFAULT 'related' COMMENT '关联类型：related-相关，reference-引用，derived-衍生',
    `similarity_score` FLOAT DEFAULT NULL COMMENT '相似度分数',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_source_entry` (`source_entry_id`),
    KEY `idx_target_entry` (`target_entry_id`),
    KEY `idx_relation_type` (`relation_type`),
    KEY `idx_similarity_score` (`similarity_score`),
    UNIQUE KEY `uk_source_target` (`source_entry_id`, `target_entry_id`),
    CONSTRAINT `fk_relation_source` FOREIGN KEY (`source_entry_id`) REFERENCES `knowledge_entry` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_relation_target` FOREIGN KEY (`target_entry_id`) REFERENCES `knowledge_entry` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='知识关联表';

-- ========================================
-- 插入初始数据
-- ========================================

-- 插入系统管理员用户
INSERT INTO `user` (`user_account`, `user_name`, `user_password`, `user_role`, `user_email`) 
VALUES ('admin', '系统管理员', '$2a$10$N.ZOn9G6/YLFixAEeNATO.K9cLL8cpGqF7mr5.trSXc1Zq7dKJtZK', 1, 'admin@timecanvas.com');

-- 插入默认日记模板
INSERT INTO `template` (`type`, `content`, `creator_id`, `tags`) VALUES
('日记', '今天是{{date}}，天气{{weather}}。\n\n**今日感悟**\n{{content}}\n\n**心情指数**: {{mood}}\n**关键词**: {{keywords}}', NULL, '日常,反思'),
('周报', '# 本周总结 ({{start_date}} - {{end_date}})\n\n## 工作完成情况\n{{work_summary}}\n\n## 学习收获\n{{learning}}\n\n## 下周计划\n{{next_week_plan}}', NULL, '总结,计划'),
('月报', '# {{month}}月总结\n\n## 月度亮点\n{{highlights}}\n\n## 数据统计\n{{statistics}}\n\n## 改进建议\n{{improvements}}', NULL, '总结,数据分析');

-- 插入系统默认提示词
INSERT INTO `prompt` (`name`, `content`, `type`, `creator_id`, `is_system`) VALUES
('日记生成', '你是一位专业的日记写作助手。请根据用户的简单描述，生成一篇完整、生动的日记。要求：1. 内容真实贴切，情感丰富 2. 语言流畅，结构清晰 3. 适当加入细节描述 4. 字数控制在200-500字之间', 'diary', NULL, 1),
('待办提取', '请从以下日记内容中提取可能的待办事项，以JSON数组格式返回，每个事项包含content（内容）、priority（优先级：0-普通,1-高,2-紧急）、due_date（预估截止日期，可为null）', 'todo', NULL, 1),
('财务分析', '请从以下日记内容中识别收入和支出信息，以JSON数组格式返回，每个项目包含item（物品）、amount（金额）、type（income或expense）、date（日期）', 'expense', NULL, 1),
('知识摘要', '请为以下知识内容生成一个简洁的摘要，控制在100字以内，突出关键信息和要点', 'knowledge', NULL, 1),
('标签提取', '请从以下内容中提取5-10个关键词作为标签，用逗号分隔返回', 'knowledge', NULL, 1);

-- 插入AI配置示例（请根据实际情况修改）
INSERT INTO `ai_config` (`chat_model`, `api_key`, `model_name`, `base_url`, `log_requests`, `log_responses`) VALUES
('doubao-lite-32k', 'YOUR_API_KEY_HERE', 'doubao-lite-32k', 'https://ark.cn-beijing.volces.com/api/v3', 1, 1),
('DeepSeek-V3.1', 'YOUR_DEEPSEEK_API_KEY', 'DeepSeek-V3.1', 'https://api.deepseek.com/v1', 1, 1);

-- 创建索引优化查询性能
CREATE INDEX `idx_diary_user_date` ON `diary` (`user_id`, `create_time`);
CREATE INDEX `idx_todo_user_status` ON `todo_list` (`user_id`, `status`);
CREATE INDEX `idx_expense_user_date` ON `expense` (`user_id`, `expense_date`);
CREATE INDEX `idx_knowledge_user_category` ON `knowledge_entry` (`user_id`, `category_id`);

-- ========================================
-- 视图创建（可选）
-- ========================================

-- 用户统计视图
CREATE VIEW `v_user_statistics` AS
SELECT 
    u.id as user_id,
    u.user_name,
    COUNT(DISTINCT d.id) as diary_count,
    COUNT(DISTINCT t.id) as todo_count,
    COUNT(DISTINCT e.id) as expense_count,
    COUNT(DISTINCT k.id) as knowledge_count,
    u.create_time as register_date
FROM `user` u
LEFT JOIN `diary` d ON u.id = d.user_id AND d.is_delete = 0
LEFT JOIN `todo_list` t ON u.id = t.user_id AND t.is_delete = 0  
LEFT JOIN `expense` e ON u.id = e.user_id AND e.is_delete = 0
LEFT JOIN `knowledge_entry` k ON u.id = k.user_id AND k.is_deleted = 0
WHERE u.is_delete = 0
GROUP BY u.id;

-- ========================================
-- 存储过程示例
-- ========================================

DELIMITER //

-- 清理软删除数据的存储过程
CREATE PROCEDURE `CleanSoftDeletedData`(IN days_ago INT)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;
    
    START TRANSACTION;
    
    -- 删除指定天数前的软删除数据
    DELETE FROM `diary` WHERE `is_delete` = 1 AND `update_time` < DATE_SUB(NOW(), INTERVAL days_ago DAY);
    DELETE FROM `todo_list` WHERE `is_delete` = 1 AND `update_time` < DATE_SUB(NOW(), INTERVAL days_ago DAY);
    DELETE FROM `expense` WHERE `is_delete` = 1 AND `update_time` < DATE_SUB(NOW(), INTERVAL days_ago DAY);
    DELETE FROM `knowledge_entry` WHERE `is_deleted` = 1 AND `updated_time` < DATE_SUB(NOW(), INTERVAL days_ago DAY);
    
    COMMIT;
END //

DELIMITER ;

-- ========================================
-- 说明
-- ========================================
/*
1. 数据库字符集使用 utf8mb4，支持 emoji 和特殊字符
2. 所有表都包含软删除字段，支持数据恢复
3. 关键字段建立了索引，优化查询性能
4. 使用外键约束保证数据完整性
5. 知识库相关表支持向量搜索和语义分析
6. 提供了默认的模板和提示词数据
7. 创建了用户统计视图，方便数据分析
8. 提供了数据清理存储过程

使用说明：
1. 请将 YOUR_API_KEY_HERE 替换为实际的 AI API 密钥
2. 根据实际需求调整表结构和索引
3. 定期执行清理存储过程，清理过期的软删除数据
4. 知识库向量字段使用 JSON 类型，如需高性能向量搜索，建议使用专门的向量数据库
*/
