package com.ruoyi.devops.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.devops.mapper.WeeklyKeyOperationDetailMapper;
import com.ruoyi.devops.domain.WeeklyKeyOperationDetail;
import com.ruoyi.devops.service.IWeeklyKeyOperationDetailService;

@Service
public class WeeklyKeyOperationDetailServiceImpl implements IWeeklyKeyOperationDetailService 
{
    @Autowired
    private WeeklyKeyOperationDetailMapper weeklyKeyOperationDetailMapper;

    @Override
    public WeeklyKeyOperationDetail selectWeeklyKeyOperationDetailById(Long id)
    {
        return weeklyKeyOperationDetailMapper.selectWeeklyKeyOperationDetailById(id);
    }

    @Override
    public List<WeeklyKeyOperationDetail> selectWeeklyKeyOperationDetailList(WeeklyKeyOperationDetail weeklyKeyOperationDetail)
    {
        return weeklyKeyOperationDetailMapper.selectWeeklyKeyOperationDetailList(weeklyKeyOperationDetail);
    }

    @Override
    public int insertWeeklyKeyOperationDetail(WeeklyKeyOperationDetail weeklyKeyOperationDetail)
    {
        weeklyKeyOperationDetail.setCreateTime(DateUtils.getNowDate());
        return weeklyKeyOperationDetailMapper.insertWeeklyKeyOperationDetail(weeklyKeyOperationDetail);
    }

    @Override
    public int updateWeeklyKeyOperationDetail(WeeklyKeyOperationDetail weeklyKeyOperationDetail)
    {
        weeklyKeyOperationDetail.setUpdateTime(DateUtils.getNowDate());
        return weeklyKeyOperationDetailMapper.updateWeeklyKeyOperationDetail(weeklyKeyOperationDetail);
    }

    @Override
    public int deleteWeeklyKeyOperationDetailByIds(Long[] ids)
    {
        return weeklyKeyOperationDetailMapper.deleteWeeklyKeyOperationDetailByIds(ids);
    }

    @Override
    public int deleteWeeklyKeyOperationDetailById(Long id)
    {
        return weeklyKeyOperationDetailMapper.deleteWeeklyKeyOperationDetailById(id);
    }
}
