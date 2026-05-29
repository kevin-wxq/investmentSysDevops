package com.ruoyi.devops.service;

import java.util.List;
import com.ruoyi.devops.domain.OpsShiftLog;

/**
 * 值班日志Service接口
 * 
 * @author ruoyi
 * @date 2026-05-30
 */
public interface IOpsShiftLogService 
{
    /**
     * 查询值班日志
     * 
     * @param id 值班日志主键
     * @return 值班日志
     */
    public OpsShiftLog selectOpsShiftLogById(Long id);

    /**
     * 查询值班日志列表
     * 
     * @param opsShiftLog 值班日志
     * @return 值班日志集合
     */
    public List<OpsShiftLog> selectOpsShiftLogList(OpsShiftLog opsShiftLog);

    /**
     * 新增值班日志
     * 
     * @param opsShiftLog 值班日志
     * @return 结果
     */
    public int insertOpsShiftLog(OpsShiftLog opsShiftLog);

    /**
     * 修改值班日志
     * 
     * @param opsShiftLog 值班日志
     * @return 结果
     */
    public int updateOpsShiftLog(OpsShiftLog opsShiftLog);

    /**
     * 批量删除值班日志
     * 
     * @param ids 需要删除的值班日志主键集合
     * @return 结果
     */
    public int deleteOpsShiftLogByIds(Long[] ids);

    /**
     * 删除值班日志信息
     * 
     * @param id 值班日志主键
     * @return 结果
     */
    public int deleteOpsShiftLogById(Long id);
}
