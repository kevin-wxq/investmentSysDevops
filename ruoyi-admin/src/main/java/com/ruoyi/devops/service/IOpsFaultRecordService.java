package com.ruoyi.devops.service;

import java.util.List;
import com.ruoyi.devops.domain.OpsFaultRecord;

/**
 * 故障记录Service接口
 * 
 * @author ruoyi
 * @date 2026-05-30
 */
public interface IOpsFaultRecordService 
{
    /**
     * 查询故障记录
     * 
     * @param id 故障记录主键
     * @return 故障记录
     */
    public OpsFaultRecord selectOpsFaultRecordById(Long id);

    /**
     * 查询故障记录列表
     * 
     * @param opsFaultRecord 故障记录
     * @return 故障记录集合
     */
    public List<OpsFaultRecord> selectOpsFaultRecordList(OpsFaultRecord opsFaultRecord);

    /**
     * 新增故障记录
     * 
     * @param opsFaultRecord 故障记录
     * @return 结果
     */
    public int insertOpsFaultRecord(OpsFaultRecord opsFaultRecord);

    /**
     * 修改故障记录
     * 
     * @param opsFaultRecord 故障记录
     * @return 结果
     */
    public int updateOpsFaultRecord(OpsFaultRecord opsFaultRecord);

    /**
     * 批量删除故障记录
     * 
     * @param ids 需要删除的故障记录主键集合
     * @return 结果
     */
    public int deleteOpsFaultRecordByIds(Long[] ids);

    /**
     * 删除故障记录信息
     * 
     * @param id 故障记录主键
     * @return 结果
     */
    public int deleteOpsFaultRecordById(Long id);
}
