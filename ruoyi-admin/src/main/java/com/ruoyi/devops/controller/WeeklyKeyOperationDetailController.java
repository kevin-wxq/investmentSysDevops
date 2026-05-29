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
import com.ruoyi.devops.domain.WeeklyKeyOperationDetail;
import com.ruoyi.devops.service.IWeeklyKeyOperationDetailService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

@RestController
@RequestMapping("/weekly/key-operation")
public class WeeklyKeyOperationDetailController extends BaseController
{
    @Autowired
    private IWeeklyKeyOperationDetailService weeklyKeyOperationDetailService;

    @PreAuthorize("@ss.hasPermi('weekly:key-operation:list')")
    @GetMapping("/list")
    public TableDataInfo list(WeeklyKeyOperationDetail weeklyKeyOperationDetail)
    {
        startPage();
        List<WeeklyKeyOperationDetail> list = weeklyKeyOperationDetailService.selectWeeklyKeyOperationDetailList(weeklyKeyOperationDetail);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('weekly:key-operation:export')")
    @Log(title = "周关键运维明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WeeklyKeyOperationDetail weeklyKeyOperationDetail)
    {
        List<WeeklyKeyOperationDetail> list = weeklyKeyOperationDetailService.selectWeeklyKeyOperationDetailList(weeklyKeyOperationDetail);
        ExcelUtil<WeeklyKeyOperationDetail> util = new ExcelUtil<WeeklyKeyOperationDetail>(WeeklyKeyOperationDetail.class);
        util.exportExcel(response, list, "周关键运维明细数据");
    }

    @PreAuthorize("@ss.hasPermi('weekly:key-operation:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(weeklyKeyOperationDetailService.selectWeeklyKeyOperationDetailById(id));
    }

    @PreAuthorize("@ss.hasPermi('weekly:key-operation:add')")
    @Log(title = "周关键运维明细", businessType = BusinessType.INSERT)
    @PostMapping(value = "/add")
    public AjaxResult add(@RequestBody WeeklyKeyOperationDetail weeklyKeyOperationDetail)
    {
        return toAjax(weeklyKeyOperationDetailService.insertWeeklyKeyOperationDetail(weeklyKeyOperationDetail));
    }

    @PreAuthorize("@ss.hasPermi('weekly:key-operation:edit')")
    @Log(title = "周关键运维明细", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/edit")
    public AjaxResult edit(@RequestBody WeeklyKeyOperationDetail weeklyKeyOperationDetail)
    {
        return toAjax(weeklyKeyOperationDetailService.updateWeeklyKeyOperationDetail(weeklyKeyOperationDetail));
    }

    @PreAuthorize("@ss.hasPermi('weekly:key-operation:remove')")
    @Log(title = "周关键运维明细", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(weeklyKeyOperationDetailService.deleteWeeklyKeyOperationDetailByIds(ids));
    }
}
