package com.ruoyi.devops.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.devops.mapper.OpsChangeRecordMapper;
import com.ruoyi.devops.domain.OpsChangeRecord;
import com.ruoyi.devops.service.IOpsChangeRecordService;

/**
 * 变更记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-05-30
 */
@Service
public class OpsChangeRecordServiceImpl implements IOpsChangeRecordService 
{
    @Autowired
    private OpsChangeRecordMapper opsChangeRecordMapper;

    /**
     * 查询变更记录
     * 
     * @param id 变更记录主键
     * @return 变更记录
     */
    @Override
    public OpsChangeRecord selectOpsChangeRecordById(Long id)
    {
        return opsChangeRecordMapper.selectOpsChangeRecordById(id);
    }

    /**
     * 查询变更记录列表
     * 
     * @param opsChangeRecord 变更记录
     * @return 变更记录
     */
    @Override
    public List<OpsChangeRecord> selectOpsChangeRecordList(OpsChangeRecord opsChangeRecord)
    {
        return opsChangeRecordMapper.selectOpsChangeRecordList(opsChangeRecord);
    }

    /**
     * 新增变更记录
     * 
     * @param opsChangeRecord 变更记录
     * @return 结果
     */
    @Override
    public int insertOpsChangeRecord(OpsChangeRecord opsChangeRecord)
    {
        opsChangeRecord.setCreateTime(DateUtils.getNowDate());
        return opsChangeRecordMapper.insertOpsChangeRecord(opsChangeRecord);
    }

    /**
     * 修改变更记录
     * 
     * @param opsChangeRecord 变更记录
     * @return 结果
     */
    @Override
    public int updateOpsChangeRecord(OpsChangeRecord opsChangeRecord)
    {
        opsChangeRecord.setUpdateTime(DateUtils.getNowDate());
        return opsChangeRecordMapper.updateOpsChangeRecord(opsChangeRecord);
    }

    /**
     * 批量删除变更记录
     * 
     * @param ids 需要删除的变更记录主键
     * @return 结果
     */
    @Override
    public int deleteOpsChangeRecordByIds(Long[] ids)
    {
        return opsChangeRecordMapper.deleteOpsChangeRecordByIds(ids);
    }

    /**
     * 删除变更记录信息
     * 
     * @param id 变更记录主键
     * @return 结果
     */
    @Override
    public int deleteOpsChangeRecordById(Long id)
    {
        return opsChangeRecordMapper.deleteOpsChangeRecordById(id);
    }
}
