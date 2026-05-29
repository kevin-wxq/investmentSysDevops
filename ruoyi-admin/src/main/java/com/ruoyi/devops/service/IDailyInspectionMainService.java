package com.ruoyi.devops.service;

import java.util.List;
import com.ruoyi.devops.domain.DailyInspectionMain;

public interface IDailyInspectionMainService 
{
    public DailyInspectionMain selectDailyInspectionMainById(Long id);
    public List<DailyInspectionMain> selectDailyInspectionMainList(DailyInspectionMain dailyInspectionMain);
    public int insertDailyInspectionMain(DailyInspectionMain dailyInspectionMain);
    public int updateDailyInspectionMain(DailyInspectionMain dailyInspectionMain);
    public int deleteDailyInspectionMainByIds(Long[] ids);
    public int deleteDailyInspectionMainById(Long id);
}
