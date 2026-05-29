package com.ruoyi.devops.mapper;

import java.util.List;
import com.ruoyi.devops.domain.DailyInspectionMain;

public interface DailyInspectionMainMapper 
{
    public DailyInspectionMain selectDailyInspectionMainById(Long id);
    public List<DailyInspectionMain> selectDailyInspectionMainList(DailyInspectionMain dailyInspectionMain);
    public int insertDailyInspectionMain(DailyInspectionMain dailyInspectionMain);
    public int updateDailyInspectionMain(DailyInspectionMain dailyInspectionMain);
    public int deleteDailyInspectionMainById(Long id);
    public int deleteDailyInspectionMainByIds(Long[] ids);
}
