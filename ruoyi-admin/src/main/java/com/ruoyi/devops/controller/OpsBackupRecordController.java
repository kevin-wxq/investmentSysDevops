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
import com.ruoyi.devops.domain.OpsBackupRecord;
import com.ruoyi.devops.service.IOpsBackupRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 备份记录Controller
 * 
 * @author ruoyi
 * @date 2026-05-30
 */
@RestController
@RequestMapping("/ops/backup")
public class OpsBackupRecordController extends BaseController
{
    @Autowired
    private IOpsBackupRecordService opsBackupRecordService;

    /**
     * 查询备份记录列表
     */
    @PreAuthorize("@ss.hasPermi('ops:backup:list')")
    @GetMapping("/list")
    public TableDataInfo list(OpsBackupRecord opsBackupRecord)
    {
        startPage();
        List<OpsBackupRecord> list = opsBackupRecordService.selectOpsBackupRecordList(opsBackupRecord);
        return getDataTable(list);
    }

    /**
     * 导出备份记录列表
     */
    @PreAuthorize("@ss.hasPermi('ops:backup:export')")
    @Log(title = "备份记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OpsBackupRecord opsBackupRecord)
    {
        List<OpsBackupRecord> list = opsBackupRecordService.selectOpsBackupRecordList(opsBackupRecord);
        ExcelUtil<OpsBackupRecord> util = new ExcelUtil<OpsBackupRecord>(OpsBackupRecord.class);
        util.exportExcel(response, list, "备份记录数据");
    }

    /**
     * 获取备份记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('ops:backup:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(opsBackupRecordService.selectOpsBackupRecordById(id));
    }

    /**
     * 新增备份记录
     */
    @PreAuthorize("@ss.hasPermi('ops:backup:add')")
    @Log(title = "备份记录", businessType = BusinessType.INSERT)
    @PostMapping(value = "/add")
    public AjaxResult add(@RequestBody OpsBackupRecord opsBackupRecord)
    {
        return toAjax(opsBackupRecordService.insertOpsBackupRecord(opsBackupRecord));
    }

    /**
     * 修改备份记录
     */
    @PreAuthorize("@ss.hasPermi('ops:backup:edit')")
    @Log(title = "备份记录", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/edit")
    public AjaxResult edit(@RequestBody OpsBackupRecord opsBackupRecord)
    {
        return toAjax(opsBackupRecordService.updateOpsBackupRecord(opsBackupRecord));
    }

    /**
     * 删除备份记录
     */
    @PreAuthorize("@ss.hasPermi('ops:backup:remove')")
    @Log(title = "备份记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(opsBackupRecordService.deleteOpsBackupRecordByIds(ids));
    }
}
