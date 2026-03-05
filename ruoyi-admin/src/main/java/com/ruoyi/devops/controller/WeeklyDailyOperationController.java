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
import com.ruoyi.devops.domain.WeeklyDailyOperation;
import com.ruoyi.devops.service.IWeeklyDailyOperationService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 日常运维明细Controller
 * 
 * @author ruoyi
 * @date 2026-03-05
 */
@RestController
@RequestMapping("/dailyOperation")
public class WeeklyDailyOperationController extends BaseController
{
    @Autowired
    private IWeeklyDailyOperationService weeklyDailyOperationService;

    /**
     * 查询日常运维明细列表
     */
    @PreAuthorize("@ss.hasPermi('daily-operation:daily-operation:list')")
    @GetMapping("/list")
    public TableDataInfo list(WeeklyDailyOperation weeklyDailyOperation)
    {
        startPage();
        List<WeeklyDailyOperation> list = weeklyDailyOperationService.selectWeeklyDailyOperationList(weeklyDailyOperation);
        return getDataTable(list);
    }

    /**
     * 导出日常运维明细列表
     */
    @PreAuthorize("@ss.hasPermi('daily-operation:daily-operation:export')")
    @Log(title = "日常运维明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WeeklyDailyOperation weeklyDailyOperation)
    {
        List<WeeklyDailyOperation> list = weeklyDailyOperationService.selectWeeklyDailyOperationList(weeklyDailyOperation);
        ExcelUtil<WeeklyDailyOperation> util = new ExcelUtil<WeeklyDailyOperation>(WeeklyDailyOperation.class);
        util.exportExcel(response, list, "日常运维明细数据");
    }

    /**
     * 获取日常运维明细详细信息
     */
    @PreAuthorize("@ss.hasPermi('daily-operation:daily-operation:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(weeklyDailyOperationService.selectWeeklyDailyOperationById(id));
    }

    /**
     * 新增日常运维明细
     */
    @PreAuthorize("@ss.hasPermi('daily-operation:daily-operation:add')")
    @Log(title = "日常运维明细", businessType = BusinessType.INSERT)
    @PostMapping(value = "/add")
    public AjaxResult add(@RequestBody WeeklyDailyOperation weeklyDailyOperation)
    {
        return toAjax(weeklyDailyOperationService.insertWeeklyDailyOperation(weeklyDailyOperation));
    }

    /**
     * 修改日常运维明细
     */
    @PreAuthorize("@ss.hasPermi('daily-operation:daily-operation:edit')")
    @Log(title = "日常运维明细", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/edit")
    public AjaxResult edit(@RequestBody WeeklyDailyOperation weeklyDailyOperation)
    {
        return toAjax(weeklyDailyOperationService.updateWeeklyDailyOperation(weeklyDailyOperation));
    }

    /**
     * 删除日常运维明细
     */
    @PreAuthorize("@ss.hasPermi('daily-operation:daily-operation:remove')")
    @Log(title = "日常运维明细", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(weeklyDailyOperationService.deleteWeeklyDailyOperationByIds(ids));
    }
}
