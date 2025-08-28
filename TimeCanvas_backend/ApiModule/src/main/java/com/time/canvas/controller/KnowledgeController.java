//package com.time.canvas.controller;
//
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.time.canvas.dto.knowledge.KnowledgeEntryAddRequest;
//import com.time.canvas.dto.knowledge.KnowledgeEntryQueryRequest;
//import com.time.canvas.model.BaseResponse;
//import com.time.canvas.model.ResultUtils;
//import com.time.canvas.service.KnowledgeService;
//import com.time.canvas.util.JwtTokenUtil;
//import com.time.canvas.vo.knowledge.KnowledgeEntryVO;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import jakarta.annotation.Resource;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.validation.Valid;
//import java.util.List;
//
///**
// * 知识库控制器
// *
// * @author wangliang
// */
//@RestController
//@RequestMapping("/api/knowledge")
//@Slf4j
//@Tag(name = "知识库管理", description = "知识库相关接口")
//public class KnowledgeController {
//
//    @Resource
//    private KnowledgeService knowledgeService;
//
//    @Resource
//    private JwtTokenUtil jwtTokenUtil;
//
//    @PostMapping("/create")
//    @Operation(summary = "创建知识条目", description = "创建新的知识条目")
//    public BaseResponse<Long> createKnowledgeEntry(@RequestBody @Valid KnowledgeEntryAddRequest addRequest,
//                                                  HttpServletRequest request) {
//        Long userId = jwtTokenUtil.getUserIdFromRequest(request);
//        Long entryId = knowledgeService.createKnowledgeEntry(addRequest, userId);
//        return ResultUtils.success(entryId);
//    }
//
//    @PostMapping("/update/{id}")
//    @Operation(summary = "更新知识条目", description = "更新指定的知识条目")
//    public BaseResponse<Boolean> updateKnowledgeEntry(@PathVariable Long id,
//                                                     @RequestBody @Valid KnowledgeEntryAddRequest addRequest,
//                                                     HttpServletRequest request) {
//        Long userId = jwtTokenUtil.getUserIdFromRequest(request);
//        boolean result = knowledgeService.updateKnowledgeEntry(id, addRequest, userId);
//        return ResultUtils.success(result);
//    }
//
//    @PostMapping("/delete/{id}")
//    @Operation(summary = "删除知识条目", description = "删除指定的知识条目")
//    public BaseResponse<Boolean> deleteKnowledgeEntry(@PathVariable Long id,
//                                                     HttpServletRequest request) {
//        Long userId = jwtTokenUtil.getUserIdFromRequest(request);
//        boolean result = knowledgeService.deleteKnowledgeEntry(id, userId);
//        return ResultUtils.success(result);
//    }
//
//    @GetMapping("/get/{id}")
//    @Operation(summary = "获取知识条目详情", description = "根据ID获取知识条目详情")
//    public BaseResponse<KnowledgeEntryVO> getKnowledgeEntry(@PathVariable Long id,
//                                                           HttpServletRequest request) {
//        Long userId = jwtTokenUtil.getUserIdFromRequest(request);
//        KnowledgeEntryVO entryVO = knowledgeService.getKnowledgeEntryVO(id, userId);
//        return ResultUtils.success(entryVO);
//    }
//
//    @PostMapping("/list")
//    @Operation(summary = "分页查询知识条目", description = "根据条件分页查询知识条目")
//    public BaseResponse<IPage<KnowledgeEntryVO>> listKnowledgeEntries(@RequestBody KnowledgeEntryQueryRequest queryRequest,
//                                                                     HttpServletRequest request) {
//        Long userId = jwtTokenUtil.getUserIdFromRequest(request);
//        queryRequest.setUserId(userId);
//        IPage<KnowledgeEntryVO> result = knowledgeService.listKnowledgeEntryVO(queryRequest);
//        return ResultUtils.success(result);
//    }
//
//    @PostMapping("/search")
//    @Operation(summary = "搜索知识条目", description = "使用关键词和语义搜索知识条目")
//    public BaseResponse<List<KnowledgeEntryVO>> searchKnowledgeEntries(@RequestBody KnowledgeEntryQueryRequest queryRequest,
//                                                                      HttpServletRequest request) {
//        Long userId = jwtTokenUtil.getUserIdFromRequest(request);
//        queryRequest.setUserId(userId);
//        List<KnowledgeEntryVO> result = knowledgeService.searchKnowledgeEntries(queryRequest);
//        return ResultUtils.success(result);
//    }
//
//    @GetMapping("/related/{id}")
//    @Operation(summary = "获取相关知识条目", description = "获取与指定条目相关的知识条目")
//    public BaseResponse<List<KnowledgeEntryVO>> getRelatedEntries(@PathVariable Long id,
//                                                                 @RequestParam(defaultValue = "5") Integer limit,
//                                                                 HttpServletRequest request) {
//        Long userId = jwtTokenUtil.getUserIdFromRequest(request);
//        List<KnowledgeEntryVO> result = knowledgeService.getRelatedEntries(id, userId, limit);
//        return ResultUtils.success(result);
//    }
//
//    @PostMapping("/extract-from-diary/{diaryId}")
//    @Operation(summary = "从日记提取知识", description = "从指定日记中提取知识条目")
//    public BaseResponse<List<KnowledgeEntryVO>> extractFromDiary(@PathVariable Long diaryId,
//                                                                HttpServletRequest request) {
//        Long userId = jwtTokenUtil.getUserIdFromRequest(request);
//        List<KnowledgeEntryVO> result = knowledgeService.extractFromDiary(diaryId, userId);
//        return ResultUtils.success(result);
//    }
//
//    @PostMapping("/qa")
//    @Operation(summary = "智能问答", description = "基于知识库的智能问答")
//    public BaseResponse<String> intelligentQA(@RequestParam String question,
//                                             HttpServletRequest request) {
//        Long userId = jwtTokenUtil.getUserIdFromRequest(request);
//        String answer = knowledgeService.intelligentQA(question, userId);
//        return ResultUtils.success(answer);
//    }
//
//    @GetMapping("/tags/popular")
//    @Operation(summary = "获取热门标签", description = "获取用户的热门标签")
//    public BaseResponse<List<String>> getPopularTags(@RequestParam(defaultValue = "20") Integer limit,
//                                                    HttpServletRequest request) {
//        Long userId = jwtTokenUtil.getUserIdFromRequest(request);
//        List<String> result = knowledgeService.getPopularTags(userId, limit);
//        return ResultUtils.success(result);
//    }
//
//    @PostMapping("/generate-summary")
//    @Operation(summary = "生成摘要", description = "为知识内容生成AI摘要")
//    public BaseResponse<String> generateSummary(@RequestParam String content) {
//        String summary = knowledgeService.generateSummary(content);
//        return ResultUtils.success(summary);
//    }
//
//    @PostMapping("/batch-import")
//    @Operation(summary = "批量导入", description = "批量导入知识条目")
//    public BaseResponse<Integer> batchImport(@RequestBody List<KnowledgeEntryAddRequest> entries,
//                                           HttpServletRequest request) {
//        Long userId = jwtTokenUtil.getUserIdFromRequest(request);
//        Integer count = knowledgeService.batchImport(entries, userId);
//        return ResultUtils.success(count);
//    }
//
//    @PostMapping("/upload")
//    @Operation(summary = "文件上传导入", description = "通过文件上传导入知识条目")
//    public BaseResponse<Integer> uploadFile(@RequestParam("file") MultipartFile file,
//                                          HttpServletRequest request) {
//        // TODO: 实现文件解析和导入逻辑
//        Long userId = jwtTokenUtil.getUserIdFromRequest(request);
//        log.info("用户 {} 上传文件: {}", userId, file.getOriginalFilename());
//        return ResultUtils.success(0, "文件上传功能待实现");
//    }
//}