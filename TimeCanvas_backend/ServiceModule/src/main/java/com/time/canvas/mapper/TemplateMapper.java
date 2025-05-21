package com.time.canvas.mapper;

import com.time.canvas.domain.Template;
import com.time.canvas.dto.Template.TemplateQueryRequest;
import com.time.canvas.vo.TemplateVO;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @author wangliang
 * @description 针对表【diary_template(日记模版表)】的数据库操作Mapper
 * @createDate 2025-05-14 14:12:43
 * @Entity generator.domain.Template
 */
public interface TemplateMapper extends BaseMapper<Template> {

    /**
     * 自定义分页查询
     *
     * @param pageParam
     * @param templateQueryRequest
     * @return
     */
    @Select("SELECT\n" +
            "    dt.id,\n" +
            "    dt.type,\n" +
            "    dt.content,\n" +
            "    dt.tags,\n" +
            "    dt.create_time,\n" +
            "    u.user_name AS creatorName\n" +
            "FROM\n" +
            "    template dt  -- 已更新表名为 template\n" +
            "LEFT JOIN\n" +
            "    user u ON dt.creator_id = u.id\n" +
            "WHERE\n" +
            "    dt.is_delete = 0 AND dt.type = 'diary'  -- 合并 WHERE 子句，并添加引号\n"
    )
    List<TemplateVO> getQuery(Page<TemplateVO> pageParam, TemplateQueryRequest templateQueryRequest);

}




