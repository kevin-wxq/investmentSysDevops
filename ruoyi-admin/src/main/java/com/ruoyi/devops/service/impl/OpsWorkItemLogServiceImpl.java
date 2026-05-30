package com.ruoyi.devops.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.devops.domain.OpsWorkItemLog;
import com.ruoyi.devops.mapper.OpsWorkItemLogMapper;
import com.ruoyi.devops.service.IOpsWorkItemLogService;

@Service
public class OpsWorkItemLogServiceImpl implements IOpsWorkItemLogService {
    @Autowired
    private OpsWorkItemLogMapper opsWorkItemLogMapper;

    @Override
    public List<OpsWorkItemLog> selectOpsWorkItemLogList(OpsWorkItemLog opsWorkItemLog) {
        return opsWorkItemLogMapper.selectOpsWorkItemLogList(opsWorkItemLog);
    }

    @Override
    public int insertOpsWorkItemLog(OpsWorkItemLog opsWorkItemLog) {
        return opsWorkItemLogMapper.insertOpsWorkItemLog(opsWorkItemLog);
    }
}
