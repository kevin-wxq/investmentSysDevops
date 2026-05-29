package com.ruoyi.devops.service;

import java.util.List;
import com.ruoyi.devops.domain.OpsChangeRecord;

/**
 * 变更记录Service接口
 * 
 * @author ruoyi
 * @date 2026-05-30
 */
public interface IOpsChangeRecordService 
{
    /**
     * 查询变更记录
     * 
     * @param id 变更记录主键
     * @return 变更记录
     */
    public OpsChangeRecord selectOpsChangeRecordById(Long id);

    /**
     * 查询变更记录列表
     * 
     * @param opsChangeRecord 变更记录
     * @return 变更记录集合
     */
    public List<OpsChangeRecord> selectOpsChangeRecordList(OpsChangeRecord opsChangeRecord);

    /**
     * 新增变更记录
     * 
     * @param opsChangeRecord 变更记录
     * @return 结果
     */
    public int insertOpsChangeRecord(OpsChangeRecord opsChangeRecord);

    /**
     * 修改变更记录
     * 
     * @param opsChangeRecord 变更记录
     * @return 结果
     */
    public int updateOpsChangeRecord(OpsChangeRecord opsChangeRecord);

    /**
     * 批量删除变更记录
     * 
     * @param ids 需要删除的变更记录主键集合
     * @return 结果
     */
    public int deleteOpsChangeRecordByIds(Long[] ids);

    /**
     * 删除变更记录信息
     * 
     * @param id 变更记录主键
     * @return 结果
     */
    public int deleteOpsChangeRecordById(Long id);
}
