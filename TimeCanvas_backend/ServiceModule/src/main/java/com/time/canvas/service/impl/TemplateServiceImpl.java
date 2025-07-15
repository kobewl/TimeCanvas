package com.time.canvas.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.time.canvas.domain.Template;
import com.time.canvas.dto.Template.TemplateQueryRequest;
import com.time.canvas.dto.Template.TemplateAddRequest;
import com.time.canvas.dto.Template.TemplateUpdateRequest;
import com.time.canvas.exception.BusinessException;
import com.time.canvas.service.TemplateService;
import com.time.canvas.mapper.TemplateMapper;
import com.time.canvas.util.ErrorCode;
import com.time.canvas.vo.TemplateVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Collections;
import com.time.canvas.util.JwtTokenUtil;
import com.time.canvas.domain.User;
import com.time.canvas.service.UserService;

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
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserService userService;


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

    @Override
    public Long addTemplate(TemplateAddRequest addRequest, String authHeader) {
        // 1. 解析token，获取userAccount
        String token = jwtTokenUtil.extractToken(authHeader);
        String userAccount = jwtTokenUtil.getUsernameFromToken(token);
        // 2. 查找用户
        User user = userService.getUserByAccount(userAccount);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }
        // 3. 构建模板对象
        Template template = new Template();
        BeanUtils.copyProperties(addRequest, template);
        template.setCreatorId(user.getId());
        template.setCreateTime(new Date());
        template.setUpdateTime(new Date());
        template.setIsDelete(0);
        boolean saved = this.save(template);
        if (!saved) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "新增模板失败");
        }
        return template.getId();
    }

    @Override
    public Boolean updateTemplate(TemplateUpdateRequest updateRequest) {
        if (updateRequest == null || updateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "模板ID不能为空");
        }
        Template template = this.getById(updateRequest.getId());
        if (template == null || template.getIsDelete() != null && template.getIsDelete() == 1) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "模板不存在");
        }
        // 只更新允许的字段
        if (updateRequest.getType() != null) template.setType(updateRequest.getType());
        if (updateRequest.getContent() != null) template.setContent(updateRequest.getContent());
        if (updateRequest.getTags() != null) template.setTags(updateRequest.getTags());
        template.setUpdateTime(new Date());
        boolean updated = this.updateById(template);
        if (!updated) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "修改模板失败");
        }
        return true;
    }

    @Override
    public Boolean deleteTemplate(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请选择要删除的模板");
        }
        for (Long id : ids) {
            Template template = this.getById(id);
            if (template != null && (template.getIsDelete() == null || template.getIsDelete() == 0)) {
                template.setIsDelete(1);
                template.setUpdateTime(new Date());
                this.updateById(template);
            }
        }
        return true;
    }

    @Override
    public TemplateVO getTemplateById(Long id) {
        Template template = this.getById(id);
        if (template == null || template.getIsDelete() != null && template.getIsDelete() == 1) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "模板不存在");
        }
        TemplateVO vo = new TemplateVO();
        BeanUtils.copyProperties(template, vo);
        // creatorName 需查user表，这里简单处理
        if (template.getCreatorId() == null) {
            vo.setCreatorName("官方创建");
        } else {
            vo.setCreatorName("用户" + template.getCreatorId()); // 可根据实际需求完善
        }
        return vo;
    }
}




