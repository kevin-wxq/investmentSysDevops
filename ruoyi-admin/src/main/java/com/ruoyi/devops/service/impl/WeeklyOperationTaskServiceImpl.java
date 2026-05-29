package com.ruoyi.devops.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.devops.mapper.WeeklyOperationTaskMapper;
import com.ruoyi.devops.domain.WeeklyOperationTask;
import com.ruoyi.devops.service.IWeeklyOperationTaskService;

@Service
public class WeeklyOperationTaskServiceImpl implements IWeeklyOperationTaskService 
{
    @Autowired
    private WeeklyOperationTaskMapper weeklyOperationTaskMapper;

    @Override
    public WeeklyOperationTask selectWeeklyOperationTaskById(Long id)
    {
        return weeklyOperationTaskMapper.selectWeeklyOperationTaskById(id);
    }

    @Override
    public List<WeeklyOperationTask> selectWeeklyOperationTaskList(WeeklyOperationTask weeklyOperationTask)
    {
        return weeklyOperationTaskMapper.selectWeeklyOperationTaskList(weeklyOperationTask);
    }

    @Override
    public int insertWeeklyOperationTask(WeeklyOperationTask weeklyOperationTask)
    {
        weeklyOperationTask.setCreateTime(DateUtils.getNowDate());
        return weeklyOperationTaskMapper.insertWeeklyOperationTask(weeklyOperationTask);
    }

    @Override
    public int updateWeeklyOperationTask(WeeklyOperationTask weeklyOperationTask)
    {
        weeklyOperationTask.setUpdateTime(DateUtils.getNowDate());
        return weeklyOperationTaskMapper.updateWeeklyOperationTask(weeklyOperationTask);
    }

    @Override
    public int deleteWeeklyOperationTaskByIds(Long[] ids)
    {
        return weeklyOperationTaskMapper.deleteWeeklyOperationTaskByIds(ids);
    }

    @Override
    public int deleteWeeklyOperationTaskById(Long id)
    {
        return weeklyOperationTaskMapper.deleteWeeklyOperationTaskById(id);
    }
}
