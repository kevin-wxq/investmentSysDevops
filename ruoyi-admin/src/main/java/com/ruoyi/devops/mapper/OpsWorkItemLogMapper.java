package com.ruoyi.devops.mapper;

import java.util.List;
import com.ruoyi.devops.domain.OpsWorkItemLog;

/**
 * 事项流转日志Mapper接口
 *
 * @author ruoyi
 * @date 2026-05-30
 */
public interface OpsWorkItemLogMapper
{
    public List<OpsWorkItemLog> selectOpsWorkItemLogList(OpsWorkItemLog opsWorkItemLog);

    public int insertOpsWorkItemLog(OpsWorkItemLog opsWorkItemLog);
}
