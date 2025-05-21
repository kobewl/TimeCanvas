package com.time.canvas.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.time.canvas.domain.Report;
import com.time.canvas.service.ReportService;
import com.time.canvas.mapper.ReportMapper;
import org.springframework.stereotype.Service;

/**
* @author wangliang
* @description 针对表【report(日报、周报、月报表)】的数据库操作Service实现
* @createDate 2025-05-14 14:12:43
*/
@Service
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report>
    implements ReportService{

}




