package com.ruoyi.devops.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.devops.mapper.DailyInspectionMainMapper;
import com.ruoyi.devops.domain.DailyInspectionMain;
import com.ruoyi.devops.service.IDailyInspectionMainService;

@Service
public class DailyInspectionMainServiceImpl implements IDailyInspectionMainService 
{
    @Autowired
    private DailyInspectionMainMapper dailyInspectionMainMapper;

    @Override
    public DailyInspectionMain selectDailyInspectionMainById(Long id)
    {
        return dailyInspectionMainMapper.selectDailyInspectionMainById(id);
    }

    @Override
    public List<DailyInspectionMain> selectDailyInspectionMainList(DailyInspectionMain dailyInspectionMain)
    {
        return dailyInspectionMainMapper.selectDailyInspectionMainList(dailyInspectionMain);
    }

    @Override
    public int insertDailyInspectionMain(DailyInspectionMain dailyInspectionMain)
    {
        dailyInspectionMain.setCreateTime(DateUtils.getNowDate());
        return dailyInspectionMainMapper.insertDailyInspectionMain(dailyInspectionMain);
    }

    @Override
    public int updateDailyInspectionMain(DailyInspectionMain dailyInspectionMain)
    {
        dailyInspectionMain.setUpdateTime(DateUtils.getNowDate());
        return dailyInspectionMainMapper.updateDailyInspectionMain(dailyInspectionMain);
    }

    @Override
    public int deleteDailyInspectionMainByIds(Long[] ids)
    {
        return dailyInspectionMainMapper.deleteDailyInspectionMainByIds(ids);
    }

    @Override
    public int deleteDailyInspectionMainById(Long id)
    {
        return dailyInspectionMainMapper.deleteDailyInspectionMainById(id);
    }
}
