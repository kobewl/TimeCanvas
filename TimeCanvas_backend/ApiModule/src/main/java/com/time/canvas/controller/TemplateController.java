package com.time.canvas.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.time.canvas.dto.Template.TemplateQueryRequest;
import com.time.canvas.service.TemplateService;
import com.time.canvas.vo.TemplateVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/template")
@Tag(name = "日记模板")
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    @PostMapping("/list")
    public Page<TemplateVO> list(@RequestBody TemplateQueryRequest templateQueryRequest) {

        // 分页查询
        Page<TemplateVO> pageParam = new Page<>(templateQueryRequest.getCurrent(), templateQueryRequest.getPageSize());
        Page<TemplateVO> templateVOPage = templateService.getTemplateList(pageParam, templateQueryRequest);

        return templateVOPage;
    }

}