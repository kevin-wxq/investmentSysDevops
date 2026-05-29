package com.ruoyi.devops.service;

import java.util.List;
import com.ruoyi.devops.domain.OpsBackupRecord;

/**
 * 备份记录Service接口
 * 
 * @author ruoyi
 * @date 2026-05-30
 */
public interface IOpsBackupRecordService 
{
    /**
     * 查询备份记录
     * 
     * @param id 备份记录主键
     * @return 备份记录
     */
    public OpsBackupRecord selectOpsBackupRecordById(Long id);

    /**
     * 查询备份记录列表
     * 
     * @param opsBackupRecord 备份记录
     * @return 备份记录集合
     */
    public List<OpsBackupRecord> selectOpsBackupRecordList(OpsBackupRecord opsBackupRecord);

    /**
     * 新增备份记录
     * 
     * @param opsBackupRecord 备份记录
     * @return 结果
     */
    public int insertOpsBackupRecord(OpsBackupRecord opsBackupRecord);

    /**
     * 修改备份记录
     * 
     * @param opsBackupRecord 备份记录
     * @return 结果
     */
    public int updateOpsBackupRecord(OpsBackupRecord opsBackupRecord);

    /**
     * 批量删除备份记录
     * 
     * @param ids 需要删除的备份记录主键集合
     * @return 结果
     */
    public int deleteOpsBackupRecordByIds(Long[] ids);

    /**
     * 删除备份记录信息
     * 
     * @param id 备份记录主键
     * @return 结果
     */
    public int deleteOpsBackupRecordById(Long id);
}
