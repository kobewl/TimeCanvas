package com.time.canvas.controller;

import com.time.canvas.domain.Diary;
import com.time.canvas.exception.BusinessException;
import com.time.canvas.model.ChatForm;
import com.time.canvas.model.SaveDiary;
import com.time.canvas.service.DiaryService;
import com.time.canvas.util.BaseResponse;
import com.time.canvas.util.ErrorCode;
import com.time.canvas.util.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/diary")
@Tag(name = "日记")
@Slf4j
public class DiaryController {

    @Autowired
    private DiaryService diaryService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping("/list")
    @Operation(summary = "获取用户日记列表")
    public BaseResponse<List<Diary>> getDiaries(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = jwtTokenUtil.extractToken(authHeader);
            String username = jwtTokenUtil.getUsernameFromToken(token);
            List<Diary> diaries = diaryService.getUserDiaries(username);
            return new BaseResponse<>(0, diaries, "获取成功");
        } catch (BusinessException e) {
            return new BaseResponse<>(e.getCode(), null, e.getMessage());
        } catch (Exception e) {
            log.error("获取用户日记列表失败: {}", e.getMessage(), e);
            return new BaseResponse<>(ErrorCode.SYSTEM_ERROR.getCode(), null, ErrorCode.SYSTEM_ERROR.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取单个日记")
    public BaseResponse<Diary> getDiaryById(@PathVariable("id") Long id, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = jwtTokenUtil.extractToken(authHeader);
            String username = jwtTokenUtil.getUsernameFromToken(token);
            Diary diary = diaryService.getUserDiaryById(id, username);
            if (diary == null) {
                return new BaseResponse<>(ErrorCode.NOT_FOUND_ERROR.getCode(), null, "日记不存在或无权访问");
            }
            return new BaseResponse<>(0, diary, "获取成功");
        } catch (BusinessException e) {
            return new BaseResponse<>(e.getCode(), null, e.getMessage());
        } catch (Exception e) {
            log.error("获取单个日记失败 (ID: {}): {}", id, e.getMessage(), e);
            return new BaseResponse<>(ErrorCode.SYSTEM_ERROR.getCode(), null, ErrorCode.SYSTEM_ERROR.getMessage());
        }
    }

    @PostMapping("/create")
    @Operation(summary = "生成日记")
    public String createDiary(@RequestBody ChatForm diary) {
        String answer = diaryService.createDiary(diary);
        return answer;
    }

    @PostMapping("/save")
    @Operation(summary = "保存日记")
    public Boolean saveDiary(@RequestBody SaveDiary saveDiary, @RequestHeader("Authorization") String authHeader) {
        Boolean saveContent = diaryService.saveDiary(saveDiary, authHeader);
        if (!saveContent){
            throw new BusinessException(ErrorCode.SAVE_ERROR);
        }
        return saveContent;
    }

}