package com.ruoyi.devops.mapper;

import java.util.List;
import com.ruoyi.devops.domain.WeeklyKeyOperationDetail;

public interface WeeklyKeyOperationDetailMapper 
{
    public WeeklyKeyOperationDetail selectWeeklyKeyOperationDetailById(Long id);
    public List<WeeklyKeyOperationDetail> selectWeeklyKeyOperationDetailList(WeeklyKeyOperationDetail weeklyKeyOperationDetail);
    public int insertWeeklyKeyOperationDetail(WeeklyKeyOperationDetail weeklyKeyOperationDetail);
    public int updateWeeklyKeyOperationDetail(WeeklyKeyOperationDetail weeklyKeyOperationDetail);
    public int deleteWeeklyKeyOperationDetailById(Long id);
    public int deleteWeeklyKeyOperationDetailByIds(Long[] ids);
}
