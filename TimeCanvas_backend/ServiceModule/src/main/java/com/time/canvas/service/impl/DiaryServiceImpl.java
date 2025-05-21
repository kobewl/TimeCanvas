package com.time.canvas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.time.canvas.assistant.DiaryAssistant;
import com.time.canvas.dao.PromptConfigDao;
import com.time.canvas.dao.TemplateConfigDao;
import com.time.canvas.domain.Diary;
import com.time.canvas.domain.Template;
import com.time.canvas.domain.Prompt;
import com.time.canvas.domain.User;
import com.time.canvas.exception.BusinessException;
import com.time.canvas.model.ChatForm;
import com.time.canvas.model.SaveDiary;
import com.time.canvas.repository.UserRepository;
import com.time.canvas.service.DiaryService;
import com.time.canvas.mapper.DiaryMapper;
import com.time.canvas.service.UserService;
import com.time.canvas.util.BaseResponse;
import com.time.canvas.util.ErrorCode;
import com.time.canvas.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.time.canvas.util.ErrorCode.PARAMS_VOID;

/**
 * @author wangliang
 * @description 针对表【diary(日记表)】的数据库操作Service实现
 * @createDate 2025-05-14 14:12:43
 */
@Service
@Slf4j
public class DiaryServiceImpl extends ServiceImpl<DiaryMapper, Diary>
        implements DiaryService {
    
    @Autowired
    private DiaryAssistant diaryAgent;

    @Autowired
    private PromptConfigDao promptConfigDao;

    @Autowired
    private TemplateConfigDao templateConfigDao;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Override
    public String createDiary(ChatForm diary) {
        try {
            if (diary == null || diary.getMessage() == null) {
                return new BaseResponse<>(PARAMS_VOID).toString();
            }
            Prompt prompt = promptConfigDao.getConfig("diary");
            String promptContent = prompt.getContent();
            Template diaryTemplate = templateConfigDao.getDiaryTemplate(diary.getTemplateId());
            String templateContent = diaryTemplate.getContent();
            String diaryChat = diaryAgent.chat("系统提示词：" + promptContent + "用户描述：" + diary.getMessage() + "要生成的日记模版是：" + templateContent + "今天的日期是：" +  new java.util.Date());
            return diaryChat;
        }catch (Exception e){
            new BusinessException(ErrorCode.SYSTEM_ERROR, "生成日记失败");
        }
        return null;
    }

    @Override
    public Boolean saveDiary(SaveDiary saveDiary, String authHeader) {

        try {
            String token = jwtTokenUtil.extractToken(authHeader);
            String username = jwtTokenUtil.getUsernameFromToken(token);
            User user = userService.getUserByAccount(username);

            Diary diary = new Diary();
            diary.setUserId(user.getId());
            diary.setTitle(saveDiary.getTitle());
            diary.setContent(saveDiary.getContent());
            diary.setTemplateId((long) saveDiary.getTemplateId());
            diary.setMood(saveDiary.getMood());
            diary.setTags(saveDiary.getTags());
            diary.setVisibility(saveDiary.getVisibility());
            return this.save(diary);

        } catch (Exception e) {
            log.error("保存日记失败: {}", e.getMessage(), e);
            new BaseResponse<>(ErrorCode.OPERATION_ERROR);
        }
        return false;
    }

    @Override
    public List<Diary> getUserDiaries(String userAccount) {
        User user = userService.getUserByAccount(userAccount);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }
        Long userId = user.getId();

        QueryWrapper<Diary> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("create_time");

        return this.list(queryWrapper);
    }

    @Override
    public Diary getUserDiaryById(Long id, String username) {
        User user = userService.getUserByAccount(username);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }
        Long userId = user.getId();

        QueryWrapper<Diary> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        queryWrapper.eq("user_id", userId);
        queryWrapper.last("LIMIT 1");

        return this.getOne(queryWrapper);
    }
}





