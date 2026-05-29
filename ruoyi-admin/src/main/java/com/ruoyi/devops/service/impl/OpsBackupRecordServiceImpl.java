package com.ruoyi.devops.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.devops.mapper.OpsBackupRecordMapper;
import com.ruoyi.devops.domain.OpsBackupRecord;
import com.ruoyi.devops.service.IOpsBackupRecordService;

/**
 * 备份记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-05-30
 */
@Service
public class OpsBackupRecordServiceImpl implements IOpsBackupRecordService 
{
    @Autowired
    private OpsBackupRecordMapper opsBackupRecordMapper;

    /**
     * 查询备份记录
     * 
     * @param id 备份记录主键
     * @return 备份记录
     */
    @Override
    public OpsBackupRecord selectOpsBackupRecordById(Long id)
    {
        return opsBackupRecordMapper.selectOpsBackupRecordById(id);
    }

    /**
     * 查询备份记录列表
     * 
     * @param opsBackupRecord 备份记录
     * @return 备份记录
     */
    @Override
    public List<OpsBackupRecord> selectOpsBackupRecordList(OpsBackupRecord opsBackupRecord)
    {
        return opsBackupRecordMapper.selectOpsBackupRecordList(opsBackupRecord);
    }

    /**
     * 新增备份记录
     * 
     * @param opsBackupRecord 备份记录
     * @return 结果
     */
    @Override
    public int insertOpsBackupRecord(OpsBackupRecord opsBackupRecord)
    {
        opsBackupRecord.setCreateTime(DateUtils.getNowDate());
        return opsBackupRecordMapper.insertOpsBackupRecord(opsBackupRecord);
    }

    /**
     * 修改备份记录
     * 
     * @param opsBackupRecord 备份记录
     * @return 结果
     */
    @Override
    public int updateOpsBackupRecord(OpsBackupRecord opsBackupRecord)
    {
        opsBackupRecord.setUpdateTime(DateUtils.getNowDate());
        return opsBackupRecordMapper.updateOpsBackupRecord(opsBackupRecord);
    }

    /**
     * 批量删除备份记录
     * 
     * @param ids 需要删除的备份记录主键
     * @return 结果
     */
    @Override
    public int deleteOpsBackupRecordByIds(Long[] ids)
    {
        return opsBackupRecordMapper.deleteOpsBackupRecordByIds(ids);
    }

    /**
     * 删除备份记录信息
     * 
     * @param id 备份记录主键
     * @return 结果
     */
    @Override
    public int deleteOpsBackupRecordById(Long id)
    {
        return opsBackupRecordMapper.deleteOpsBackupRecordById(id);
    }
}
