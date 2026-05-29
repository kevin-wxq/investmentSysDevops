package com.ruoyi.devops.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.devops.domain.DailyInspectionMain;
import com.ruoyi.devops.service.IDailyInspectionMainService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

@RestController
@RequestMapping("/daily/inspection")
public class DailyInspectionMainController extends BaseController
{
    @Autowired
    private IDailyInspectionMainService dailyInspectionMainService;

    @PreAuthorize("@ss.hasPermi('daily:inspection:list')")
    @GetMapping("/list")
    public TableDataInfo list(DailyInspectionMain dailyInspectionMain)
    {
        startPage();
        List<DailyInspectionMain> list = dailyInspectionMainService.selectDailyInspectionMainList(dailyInspectionMain);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('daily:inspection:export')")
    @Log(title = "日常巡检主表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DailyInspectionMain dailyInspectionMain)
    {
        List<DailyInspectionMain> list = dailyInspectionMainService.selectDailyInspectionMainList(dailyInspectionMain);
        ExcelUtil<DailyInspectionMain> util = new ExcelUtil<DailyInspectionMain>(DailyInspectionMain.class);
        util.exportExcel(response, list, "日常巡检主表数据");
    }

    @PreAuthorize("@ss.hasPermi('daily:inspection:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(dailyInspectionMainService.selectDailyInspectionMainById(id));
    }

    @PreAuthorize("@ss.hasPermi('daily:inspection:add')")
    @Log(title = "日常巡检主表", businessType = BusinessType.INSERT)
    @PostMapping(value = "/add")
    public AjaxResult add(@RequestBody DailyInspectionMain dailyInspectionMain)
    {
        return toAjax(dailyInspectionMainService.insertDailyInspectionMain(dailyInspectionMain));
    }

    @PreAuthorize("@ss.hasPermi('daily:inspection:edit')")
    @Log(title = "日常巡检主表", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/edit")
    public AjaxResult edit(@RequestBody DailyInspectionMain dailyInspectionMain)
    {
        return toAjax(dailyInspectionMainService.updateDailyInspectionMain(dailyInspectionMain));
    }

    @PreAuthorize("@ss.hasPermi('daily:inspection:remove')")
    @Log(title = "日常巡检主表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(dailyInspectionMainService.deleteDailyInspectionMainByIds(ids));
    }
}
