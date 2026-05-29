package com.ruoyi.devops.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.devops.mapper.OpsShiftLogMapper;
import com.ruoyi.devops.domain.OpsShiftLog;
import com.ruoyi.devops.service.IOpsShiftLogService;

/**
 * 值班日志Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-05-30
 */
@Service
public class OpsShiftLogServiceImpl implements IOpsShiftLogService 
{
    @Autowired
    private OpsShiftLogMapper opsShiftLogMapper;

    /**
     * 查询值班日志
     * 
     * @param id 值班日志主键
     * @return 值班日志
     */
    @Override
    public OpsShiftLog selectOpsShiftLogById(Long id)
    {
        return opsShiftLogMapper.selectOpsShiftLogById(id);
    }

    /**
     * 查询值班日志列表
     * 
     * @param opsShiftLog 值班日志
     * @return 值班日志
     */
    @Override
    public List<OpsShiftLog> selectOpsShiftLogList(OpsShiftLog opsShiftLog)
    {
        return opsShiftLogMapper.selectOpsShiftLogList(opsShiftLog);
    }

    /**
     * 新增值班日志
     * 
     * @param opsShiftLog 值班日志
     * @return 结果
     */
    @Override
    public int insertOpsShiftLog(OpsShiftLog opsShiftLog)
    {
        opsShiftLog.setCreateTime(DateUtils.getNowDate());
        return opsShiftLogMapper.insertOpsShiftLog(opsShiftLog);
    }

    /**
     * 修改值班日志
     * 
     * @param opsShiftLog 值班日志
     * @return 结果
     */
    @Override
    public int updateOpsShiftLog(OpsShiftLog opsShiftLog)
    {
        opsShiftLog.setUpdateTime(DateUtils.getNowDate());
        return opsShiftLogMapper.updateOpsShiftLog(opsShiftLog);
    }

    /**
     * 批量删除值班日志
     * 
     * @param ids 需要删除的值班日志主键
     * @return 结果
     */
    @Override
    public int deleteOpsShiftLogByIds(Long[] ids)
    {
        return opsShiftLogMapper.deleteOpsShiftLogByIds(ids);
    }

    /**
     * 删除值班日志信息
     * 
     * @param id 值班日志主键
     * @return 结果
     */
    @Override
    public int deleteOpsShiftLogById(Long id)
    {
        return opsShiftLogMapper.deleteOpsShiftLogById(id);
    }
}
