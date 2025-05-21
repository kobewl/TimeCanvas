package com.time.canvas.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.time.canvas.domain.Template;
import com.time.canvas.dto.Template.TemplateQueryRequest;
import com.time.canvas.exception.BusinessException;
import com.time.canvas.service.TemplateService;
import com.time.canvas.mapper.TemplateMapper;
import com.time.canvas.util.ErrorCode;
import com.time.canvas.vo.TemplateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author wangliang
* @description 针对表【diary_template(日记模版表)】的数据库操作Service实现
* @createDate 2025-05-14 14:12:43
*/
@Service
public class TemplateServiceImpl extends ServiceImpl<TemplateMapper, Template>
    implements TemplateService {

    @Autowired
    private TemplateMapper templateMapper;


    @Override
    public Page<TemplateVO> getTemplateList(Page<TemplateVO> pageParam, TemplateQueryRequest templateQueryRequest) {

        // 自定义分页查询
        List<TemplateVO> templateVO = templateMapper.getQuery(pageParam, templateQueryRequest);
        if (templateVO == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "日记模版分页查询失败");
        }
        // 设置分页结果
        pageParam.setRecords(templateVO);
        pageParam.setTotal(templateVO.size());
        // 如果查询出来的创建者为空，则设置为官方创建
        for (TemplateVO template : templateVO) {
            if (template.getCreatorName() == null) {
                template.setCreatorName("官方创建");
            }
        }

        return pageParam;
    }
}




