package com.ruoyi.devops.service;

import java.util.List;
import com.ruoyi.devops.domain.WeeklyOperationTask;

public interface IWeeklyOperationTaskService 
{
    public WeeklyOperationTask selectWeeklyOperationTaskById(Long id);
    public List<WeeklyOperationTask> selectWeeklyOperationTaskList(WeeklyOperationTask weeklyOperationTask);
    public int insertWeeklyOperationTask(WeeklyOperationTask weeklyOperationTask);
    public int updateWeeklyOperationTask(WeeklyOperationTask weeklyOperationTask);
    public int deleteWeeklyOperationTaskByIds(Long[] ids);
    public int deleteWeeklyOperationTaskById(Long id);
}
