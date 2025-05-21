package com.time.canvas.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.time.canvas.domain.Expense;
import com.time.canvas.service.ExpenseService;
import com.time.canvas.mapper.ExpenseMapper;
import org.springframework.stereotype.Service;

/**
* @author wangliang
* @description 针对表【expense(开支表)】的数据库操作Service实现
* @createDate 2025-05-14 14:12:43
*/
@Service
public class ExpenseServiceImpl extends ServiceImpl<ExpenseMapper, Expense>
    implements ExpenseService{

}




