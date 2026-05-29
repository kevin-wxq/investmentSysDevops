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
import com.ruoyi.devops.domain.WeeklyOperationTask;
import com.ruoyi.devops.service.IWeeklyOperationTaskService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

@RestController
@RequestMapping("/weekly/operation-task")
public class WeeklyOperationTaskController extends BaseController
{
    @Autowired
    private IWeeklyOperationTaskService weeklyOperationTaskService;

    @PreAuthorize("@ss.hasPermi('weekly:operation-task:list')")
    @GetMapping("/list")
    public TableDataInfo list(WeeklyOperationTask weeklyOperationTask)
    {
        startPage();
        List<WeeklyOperationTask> list = weeklyOperationTaskService.selectWeeklyOperationTaskList(weeklyOperationTask);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('weekly:operation-task:export')")
    @Log(title = "周运维任务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WeeklyOperationTask weeklyOperationTask)
    {
        List<WeeklyOperationTask> list = weeklyOperationTaskService.selectWeeklyOperationTaskList(weeklyOperationTask);
        ExcelUtil<WeeklyOperationTask> util = new ExcelUtil<WeeklyOperationTask>(WeeklyOperationTask.class);
        util.exportExcel(response, list, "周运维任务数据");
    }

    @PreAuthorize("@ss.hasPermi('weekly:operation-task:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(weeklyOperationTaskService.selectWeeklyOperationTaskById(id));
    }

    @PreAuthorize("@ss.hasPermi('weekly:operation-task:add')")
    @Log(title = "周运维任务", businessType = BusinessType.INSERT)
    @PostMapping(value = "/add")
    public AjaxResult add(@RequestBody WeeklyOperationTask weeklyOperationTask)
    {
        return toAjax(weeklyOperationTaskService.insertWeeklyOperationTask(weeklyOperationTask));
    }

    @PreAuthorize("@ss.hasPermi('weekly:operation-task:edit')")
    @Log(title = "周运维任务", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/edit")
    public AjaxResult edit(@RequestBody WeeklyOperationTask weeklyOperationTask)
    {
        return toAjax(weeklyOperationTaskService.updateWeeklyOperationTask(weeklyOperationTask));
    }

    @PreAuthorize("@ss.hasPermi('weekly:operation-task:remove')")
    @Log(title = "周运维任务", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(weeklyOperationTaskService.deleteWeeklyOperationTaskByIds(ids));
    }
}
