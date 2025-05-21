package com.time.canvas.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.time.canvas.domain.TodoList;
import com.time.canvas.service.TodoListService;
import com.time.canvas.mapper.TodoListMapper;
import org.springframework.stereotype.Service;

/**
* @author wangliang
* @description 针对表【todo_list(Todo List 表)】的数据库操作Service实现
* @createDate 2025-05-14 14:12:43
*/
@Service
public class TodoListServiceImpl extends ServiceImpl<TodoListMapper, TodoList>
    implements TodoListService{

}




