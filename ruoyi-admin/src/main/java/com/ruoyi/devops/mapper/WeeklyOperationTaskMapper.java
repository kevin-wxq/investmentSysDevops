package com.ruoyi.devops.mapper;

import java.util.List;
import com.ruoyi.devops.domain.WeeklyOperationTask;

public interface WeeklyOperationTaskMapper 
{
    public WeeklyOperationTask selectWeeklyOperationTaskById(Long id);
    public List<WeeklyOperationTask> selectWeeklyOperationTaskList(WeeklyOperationTask weeklyOperationTask);
    public int insertWeeklyOperationTask(WeeklyOperationTask weeklyOperationTask);
    public int updateWeeklyOperationTask(WeeklyOperationTask weeklyOperationTask);
    public int deleteWeeklyOperationTaskById(Long id);
    public int deleteWeeklyOperationTaskByIds(Long[] ids);
}
