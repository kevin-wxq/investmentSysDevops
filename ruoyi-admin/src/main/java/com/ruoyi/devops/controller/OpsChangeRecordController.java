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
import com.ruoyi.devops.domain.OpsChangeRecord;
import com.ruoyi.devops.service.IOpsChangeRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 变更记录Controller
 * 
 * @author ruoyi
 * @date 2026-05-30
 */
@RestController
@RequestMapping("/ops/change")
public class OpsChangeRecordController extends BaseController
{
    @Autowired
    private IOpsChangeRecordService opsChangeRecordService;

    /**
     * 查询变更记录列表
     */
    @PreAuthorize("@ss.hasPermi('ops:change:list')")
    @GetMapping("/list")
    public TableDataInfo list(OpsChangeRecord opsChangeRecord)
    {
        startPage();
        List<OpsChangeRecord> list = opsChangeRecordService.selectOpsChangeRecordList(opsChangeRecord);
        return getDataTable(list);
    }

    /**
     * 导出变更记录列表
     */
    @PreAuthorize("@ss.hasPermi('ops:change:export')")
    @Log(title = "变更记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OpsChangeRecord opsChangeRecord)
    {
        List<OpsChangeRecord> list = opsChangeRecordService.selectOpsChangeRecordList(opsChangeRecord);
        ExcelUtil<OpsChangeRecord> util = new ExcelUtil<OpsChangeRecord>(OpsChangeRecord.class);
        util.exportExcel(response, list, "变更记录数据");
    }

    /**
     * 获取变更记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('ops:change:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(opsChangeRecordService.selectOpsChangeRecordById(id));
    }

    /**
     * 新增变更记录
     */
    @PreAuthorize("@ss.hasPermi('ops:change:add')")
    @Log(title = "变更记录", businessType = BusinessType.INSERT)
    @PostMapping(value = "/add")
    public AjaxResult add(@RequestBody OpsChangeRecord opsChangeRecord)
    {
        return toAjax(opsChangeRecordService.insertOpsChangeRecord(opsChangeRecord));
    }

    /**
     * 修改变更记录
     */
    @PreAuthorize("@ss.hasPermi('ops:change:edit')")
    @Log(title = "变更记录", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/edit")
    public AjaxResult edit(@RequestBody OpsChangeRecord opsChangeRecord)
    {
        return toAjax(opsChangeRecordService.updateOpsChangeRecord(opsChangeRecord));
    }

    /**
     * 删除变更记录
     */
    @PreAuthorize("@ss.hasPermi('ops:change:remove')")
    @Log(title = "变更记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(opsChangeRecordService.deleteOpsChangeRecordByIds(ids));
    }
}
