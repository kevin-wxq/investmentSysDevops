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
import com.ruoyi.devops.domain.OpsShiftLog;
import com.ruoyi.devops.service.IOpsShiftLogService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 值班日志Controller
 * 
 * @author ruoyi
 * @date 2026-05-30
 */
@RestController
@RequestMapping("/ops/shift-log")
public class OpsShiftLogController extends BaseController
{
    @Autowired
    private IOpsShiftLogService opsShiftLogService;

    /**
     * 查询值班日志列表
     */
    @PreAuthorize("@ss.hasPermi('ops:shift-log:list')")
    @GetMapping("/list")
    public TableDataInfo list(OpsShiftLog opsShiftLog)
    {
        startPage();
        List<OpsShiftLog> list = opsShiftLogService.selectOpsShiftLogList(opsShiftLog);
        return getDataTable(list);
    }

    /**
     * 导出值班日志列表
     */
    @PreAuthorize("@ss.hasPermi('ops:shift-log:export')")
    @Log(title = "值班日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OpsShiftLog opsShiftLog)
    {
        List<OpsShiftLog> list = opsShiftLogService.selectOpsShiftLogList(opsShiftLog);
        ExcelUtil<OpsShiftLog> util = new ExcelUtil<OpsShiftLog>(OpsShiftLog.class);
        util.exportExcel(response, list, "值班日志数据");
    }

    /**
     * 获取值班日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('ops:shift-log:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(opsShiftLogService.selectOpsShiftLogById(id));
    }

    /**
     * 新增值班日志
     */
    @PreAuthorize("@ss.hasPermi('ops:shift-log:add')")
    @Log(title = "值班日志", businessType = BusinessType.INSERT)
    @PostMapping(value = "/add")
    public AjaxResult add(@RequestBody OpsShiftLog opsShiftLog)
    {
        return toAjax(opsShiftLogService.insertOpsShiftLog(opsShiftLog));
    }

    /**
     * 修改值班日志
     */
    @PreAuthorize("@ss.hasPermi('ops:shift-log:edit')")
    @Log(title = "值班日志", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/edit")
    public AjaxResult edit(@RequestBody OpsShiftLog opsShiftLog)
    {
        return toAjax(opsShiftLogService.updateOpsShiftLog(opsShiftLog));
    }

    /**
     * 删除值班日志
     */
    @PreAuthorize("@ss.hasPermi('ops:shift-log:remove')")
    @Log(title = "值班日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(opsShiftLogService.deleteOpsShiftLogByIds(ids));
    }
}
