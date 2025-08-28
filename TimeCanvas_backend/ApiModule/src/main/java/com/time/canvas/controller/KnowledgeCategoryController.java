package com.time.canvas.controller;

import com.time.canvas.dto.knowledge.KnowledgeCategoryAddRequest;
import com.time.canvas.dto.knowledge.KnowledgeCategoryUpdateRequest;
import com.time.canvas.service.KnowledgeCategoryService;
import com.time.canvas.util.BaseResponse;
import com.time.canvas.util.JwtTokenUtil;
import com.time.canvas.util.ResultUtils;
import com.time.canvas.vo.knowledge.KnowledgeCategoryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;

/**
 * 知识分类控制器
 *
 * @author wangliang
 */
@RestController
@RequestMapping("/api/knowledge/category")
@Slf4j
@Tag(name = "知识分类管理", description = "知识分类相关接口")
public class KnowledgeCategoryController {

    @Resource
    private KnowledgeCategoryService knowledgeCategoryService;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/create")
    @Operation(summary = "创建知识分类", description = "创建新的知识分类")
    public BaseResponse<Long> createCategory(@RequestBody @Valid KnowledgeCategoryAddRequest addRequest,
                                            HttpServletRequest request) {
        Long userId = jwtTokenUtil.getUserIdFromRequest(request);
        Long categoryId = knowledgeCategoryService.createCategory(addRequest, userId);
        return ResultUtils.success(categoryId);
    }

    @PostMapping("/update")
    @Operation(summary = "更新知识分类", description = "更新指定的知识分类")
    public BaseResponse<Boolean> updateCategory(@RequestBody @Valid KnowledgeCategoryUpdateRequest updateRequest,
                                               HttpServletRequest request) {
        Long userId = jwtTokenUtil.getUserIdFromRequest(request);
        boolean result = knowledgeCategoryService.updateCategory(updateRequest.getId(), updateRequest, userId);
        return ResultUtils.success(result);
    }

    @PostMapping("/delete/{id}")
    @Operation(summary = "删除知识分类", description = "删除指定的知识分类")
    public BaseResponse<Boolean> deleteCategory(@PathVariable Long id,
                                               HttpServletRequest request) {
        Long userId = jwtTokenUtil.getUserIdFromRequest(request);
        boolean result = knowledgeCategoryService.deleteCategory(id, userId);
        return ResultUtils.success(result);
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "获取分类详情", description = "根据ID获取知识分类详情")
    public BaseResponse<KnowledgeCategoryVO> getCategory(@PathVariable Long id,
                                                        HttpServletRequest request) {
        Long userId = jwtTokenUtil.getUserIdFromRequest(request);
        KnowledgeCategoryVO categoryVO = knowledgeCategoryService.getCategoryVO(id, userId);
        return ResultUtils.success(categoryVO);
    }

    @GetMapping("/tree")
    @Operation(summary = "获取分类树", description = "获取用户的完整分类树")
    public BaseResponse<List<KnowledgeCategoryVO>> getCategoryTree(HttpServletRequest request) {
        Long userId = jwtTokenUtil.getUserIdFromRequest(request);
        List<KnowledgeCategoryVO> categoryTree = knowledgeCategoryService.getCategoryTree(userId);
        return ResultUtils.success(categoryTree);
    }

    @GetMapping("/children/{id}")
    @Operation(summary = "获取子分类ID", description = "获取指定分类的所有子分类ID")
    public BaseResponse<List<Long>> getChildCategoryIds(@PathVariable Long id,
                                                       HttpServletRequest request) {
        Long userId = jwtTokenUtil.getUserIdFromRequest(request);
        List<Long> childIds = knowledgeCategoryService.getChildCategoryIds(id, userId);
        return ResultUtils.success(childIds);
    }
}