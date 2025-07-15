package com.time.canvas.util;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 删除请求，支持单个或批量
 * @author wangliang
 */
@Data
public class DeleteRequest implements Serializable {
    /** 单个id（兼容老用法） */
    private Long id;
    /** 批量id列表 */
    private List<Long> ids;
    private static final long serialVersionUID = 1L;
}