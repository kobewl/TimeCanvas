package com.time.canvas.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.time.canvas.dto.Template.TemplateQueryRequest;
import com.time.canvas.dto.Template.TemplateAddRequest;
import com.time.canvas.dto.Template.TemplateUpdateRequest;
import com.time.canvas.service.TemplateService;
import com.time.canvas.vo.TemplateVO;
import com.time.canvas.util.BaseResponse;
import com.time.canvas.util.ResultUtils;
import com.time.canvas.util.DeleteRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/template")
@Tag(name = "日记模板")
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    /**
     * 新增日记模板
     */
    @PostMapping("/add")
    public BaseResponse<Long> addTemplate(@RequestBody @Validated TemplateAddRequest addRequest,
                                          @RequestHeader("Authorization") String authHeader) {
        Long id = templateService.addTemplate(addRequest, authHeader);
        return ResultUtils.success(id);
    }

    /**
     * 修改日记模板
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateTemplate(@RequestBody @Validated TemplateUpdateRequest updateRequest) {
        Boolean result = templateService.updateTemplate(updateRequest);
        return ResultUtils.success(result);
    }

    /**
     * 删除日记模板（支持批量）
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTemplate(@RequestBody DeleteRequest deleteRequest) {
        // 支持批量和单个删除
        java.util.List<Long> ids = deleteRequest.getIds() != null ? deleteRequest.getIds() : java.util.Collections.singletonList(deleteRequest.getId());
        Boolean result = templateService.deleteTemplate(ids);
        return ResultUtils.success(result);
    }

    /**
     * 查询模板详情
     */
    @GetMapping("/get/{id}")
    public BaseResponse<TemplateVO> getTemplate(@PathVariable Long id) {
        TemplateVO vo = templateService.getTemplateById(id);
        return ResultUtils.success(vo);
    }

    @PostMapping("/list")
    public Page<TemplateVO> list(@RequestBody TemplateQueryRequest templateQueryRequest) {

        // 分页查询
        Page<TemplateVO> pageParam = new Page<>(templateQueryRequest.getCurrent(), templateQueryRequest.getPageSize());
        Page<TemplateVO> templateVOPage = templateService.getTemplateList(pageParam, templateQueryRequest);

        return templateVOPage;
    }

}