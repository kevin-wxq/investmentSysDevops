package com.ruoyi.devops.service;

import java.util.List;
import com.ruoyi.devops.domain.WeeklyDailyOperation;

/**
 * 日常运维明细Service接口
 * 
 * @author ruoyi
 * @date 2026-03-05
 */
public interface IWeeklyDailyOperationService 
{
    /**
     * 查询日常运维明细
     * 
     * @param id 日常运维明细主键
     * @return 日常运维明细
     */
    public WeeklyDailyOperation selectWeeklyDailyOperationById(Long id);

    /**
     * 查询日常运维明细列表
     * 
     * @param weeklyDailyOperation 日常运维明细
     * @return 日常运维明细集合
     */
    public List<WeeklyDailyOperation> selectWeeklyDailyOperationList(WeeklyDailyOperation weeklyDailyOperation);

    /**
     * 新增日常运维明细
     * 
     * @param weeklyDailyOperation 日常运维明细
     * @return 结果
     */
    public int insertWeeklyDailyOperation(WeeklyDailyOperation weeklyDailyOperation);

    /**
     * 修改日常运维明细
     * 
     * @param weeklyDailyOperation 日常运维明细
     * @return 结果
     */
    public int updateWeeklyDailyOperation(WeeklyDailyOperation weeklyDailyOperation);

    /**
     * 批量删除日常运维明细
     * 
     * @param ids 需要删除的日常运维明细主键集合
     * @return 结果
     */
    public int deleteWeeklyDailyOperationByIds(Long[] ids);

    /**
     * 删除日常运维明细信息
     * 
     * @param id 日常运维明细主键
     * @return 结果
     */
    public int deleteWeeklyDailyOperationById(Long id);
}
