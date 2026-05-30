package com.ruoyi.devops.service;

import java.util.List;
import com.ruoyi.devops.domain.OpsWorkItemLog;

/**
 * 事项流转日志Service接口
 *
 * @author ruoyi
 * @date 2026-05-30
 */
public interface IOpsWorkItemLogService
{
    public List<OpsWorkItemLog> selectOpsWorkItemLogList(OpsWorkItemLog opsWorkItemLog);

    public int insertOpsWorkItemLog(OpsWorkItemLog opsWorkItemLog);
}
