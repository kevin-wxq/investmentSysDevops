package com.ruoyi.devops.service;

import java.util.List;
import com.ruoyi.devops.domain.WeeklyKeyOperationDetail;

public interface IWeeklyKeyOperationDetailService 
{
    public WeeklyKeyOperationDetail selectWeeklyKeyOperationDetailById(Long id);
    public List<WeeklyKeyOperationDetail> selectWeeklyKeyOperationDetailList(WeeklyKeyOperationDetail weeklyKeyOperationDetail);
    public int insertWeeklyKeyOperationDetail(WeeklyKeyOperationDetail weeklyKeyOperationDetail);
    public int updateWeeklyKeyOperationDetail(WeeklyKeyOperationDetail weeklyKeyOperationDetail);
    public int deleteWeeklyKeyOperationDetailByIds(Long[] ids);
    public int deleteWeeklyKeyOperationDetailById(Long id);
}
