package com.time.canvas.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.time.canvas.domain.Template;
import com.baomidou.mybatisplus.extension.service.IService;
import com.time.canvas.dto.Template.TemplateQueryRequest;
import com.time.canvas.vo.TemplateVO;

/**
* @author wangliang
* @description 针对表【diary_template(日记模版表)】的数据库操作Service
* @createDate 2025-05-14 14:12:43
*/
public interface TemplateService extends IService<Template> {

    Page<TemplateVO> getTemplateList(Page<TemplateVO> pageParam, TemplateQueryRequest templateQueryRequest);
}
