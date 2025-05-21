package com.time.canvas.service;

import com.time.canvas.domain.Diary;
import com.baomidou.mybatisplus.extension.service.IService;
import com.time.canvas.model.ChatForm;
import com.time.canvas.model.SaveDiary;

import java.util.List;

/**
* @author wangliang
* @description 针对表【diary(日记表)】的数据库操作Service
* @createDate 2025-05-14 14:12:43
*/
public interface DiaryService extends IService<Diary> {

    String createDiary(ChatForm diary);

    Boolean saveDiary(SaveDiary saveDiary, String authHeader);

    List<Diary> getUserDiaries(String username);

    Diary getUserDiaryById(Long id, String username);
}
