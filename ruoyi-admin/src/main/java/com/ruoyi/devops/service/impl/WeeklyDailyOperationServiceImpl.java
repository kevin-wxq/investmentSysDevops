package com.ruoyi.devops.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.devops.mapper.WeeklyDailyOperationMapper;
import com.ruoyi.devops.domain.WeeklyDailyOperation;
import com.ruoyi.devops.service.IWeeklyDailyOperationService;

/**
 * 日常运维明细Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-05
 */
@Service
public class WeeklyDailyOperationServiceImpl implements IWeeklyDailyOperationService 
{
    @Autowired
    private WeeklyDailyOperationMapper weeklyDailyOperationMapper;

    /**
     * 查询日常运维明细
     * 
     * @param id 日常运维明细主键
     * @return 日常运维明细
     */
    @Override
    public WeeklyDailyOperation selectWeeklyDailyOperationById(Long id)
    {
        return weeklyDailyOperationMapper.selectWeeklyDailyOperationById(id);
    }

    /**
     * 查询日常运维明细列表
     * 
     * @param weeklyDailyOperation 日常运维明细
     * @return 日常运维明细
     */
    @Override
    public List<WeeklyDailyOperation> selectWeeklyDailyOperationList(WeeklyDailyOperation weeklyDailyOperation)
    {
        return weeklyDailyOperationMapper.selectWeeklyDailyOperationList(weeklyDailyOperation);
    }

    /**
     * 新增日常运维明细
     * 
     * @param weeklyDailyOperation 日常运维明细
     * @return 结果
     */
    @Override
    public int insertWeeklyDailyOperation(WeeklyDailyOperation weeklyDailyOperation)
    {
        weeklyDailyOperation.setCreateTime(DateUtils.getNowDate());
        return weeklyDailyOperationMapper.insertWeeklyDailyOperation(weeklyDailyOperation);
    }

    /**
     * 修改日常运维明细
     * 
     * @param weeklyDailyOperation 日常运维明细
     * @return 结果
     */
    @Override
    public int updateWeeklyDailyOperation(WeeklyDailyOperation weeklyDailyOperation)
    {
        weeklyDailyOperation.setUpdateTime(DateUtils.getNowDate());
        return weeklyDailyOperationMapper.updateWeeklyDailyOperation(weeklyDailyOperation);
    }

    /**
     * 批量删除日常运维明细
     * 
     * @param ids 需要删除的日常运维明细主键
     * @return 结果
     */
    @Override
    public int deleteWeeklyDailyOperationByIds(Long[] ids)
    {
        return weeklyDailyOperationMapper.deleteWeeklyDailyOperationByIds(ids);
    }

    /**
     * 删除日常运维明细信息
     * 
     * @param id 日常运维明细主键
     * @return 结果
     */
    @Override
    public int deleteWeeklyDailyOperationById(Long id)
    {
        return weeklyDailyOperationMapper.deleteWeeklyDailyOperationById(id);
    }
}
