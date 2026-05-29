package com.ruoyi.devops.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.devops.mapper.OpsFaultRecordMapper;
import com.ruoyi.devops.domain.OpsFaultRecord;
import com.ruoyi.devops.service.IOpsFaultRecordService;

/**
 * 故障记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-05-30
 */
@Service
public class OpsFaultRecordServiceImpl implements IOpsFaultRecordService 
{
    @Autowired
    private OpsFaultRecordMapper opsFaultRecordMapper;

    /**
     * 查询故障记录
     * 
     * @param id 故障记录主键
     * @return 故障记录
     */
    @Override
    public OpsFaultRecord selectOpsFaultRecordById(Long id)
    {
        return opsFaultRecordMapper.selectOpsFaultRecordById(id);
    }

    /**
     * 查询故障记录列表
     * 
     * @param opsFaultRecord 故障记录
     * @return 故障记录
     */
    @Override
    public List<OpsFaultRecord> selectOpsFaultRecordList(OpsFaultRecord opsFaultRecord)
    {
        return opsFaultRecordMapper.selectOpsFaultRecordList(opsFaultRecord);
    }

    /**
     * 新增故障记录
     * 
     * @param opsFaultRecord 故障记录
     * @return 结果
     */
    @Override
    public int insertOpsFaultRecord(OpsFaultRecord opsFaultRecord)
    {
        opsFaultRecord.setCreateTime(DateUtils.getNowDate());
        return opsFaultRecordMapper.insertOpsFaultRecord(opsFaultRecord);
    }

    /**
     * 修改故障记录
     * 
     * @param opsFaultRecord 故障记录
     * @return 结果
     */
    @Override
    public int updateOpsFaultRecord(OpsFaultRecord opsFaultRecord)
    {
        opsFaultRecord.setUpdateTime(DateUtils.getNowDate());
        return opsFaultRecordMapper.updateOpsFaultRecord(opsFaultRecord);
    }

    /**
     * 批量删除故障记录
     * 
     * @param ids 需要删除的故障记录主键
     * @return 结果
     */
    @Override
    public int deleteOpsFaultRecordByIds(Long[] ids)
    {
        return opsFaultRecordMapper.deleteOpsFaultRecordByIds(ids);
    }

    /**
     * 删除故障记录信息
     * 
     * @param id 故障记录主键
     * @return 结果
     */
    @Override
    public int deleteOpsFaultRecordById(Long id)
    {
        return opsFaultRecordMapper.deleteOpsFaultRecordById(id);
    }
}
