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
import com.ruoyi.devops.domain.OpsFaultRecord;
import com.ruoyi.devops.service.IOpsFaultRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 故障记录Controller
 * 
 * @author ruoyi
 * @date 2026-05-30
 */
@RestController
@RequestMapping("/ops/fault")
public class OpsFaultRecordController extends BaseController
{
    @Autowired
    private IOpsFaultRecordService opsFaultRecordService;

    /**
     * 查询故障记录列表
     */
    @PreAuthorize("@ss.hasPermi('ops:fault:list')")
    @GetMapping("/list")
    public TableDataInfo list(OpsFaultRecord opsFaultRecord)
    {
        startPage();
        List<OpsFaultRecord> list = opsFaultRecordService.selectOpsFaultRecordList(opsFaultRecord);
        return getDataTable(list);
    }

    /**
     * 导出故障记录列表
     */
    @PreAuthorize("@ss.hasPermi('ops:fault:export')")
    @Log(title = "故障记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OpsFaultRecord opsFaultRecord)
    {
        List<OpsFaultRecord> list = opsFaultRecordService.selectOpsFaultRecordList(opsFaultRecord);
        ExcelUtil<OpsFaultRecord> util = new ExcelUtil<OpsFaultRecord>(OpsFaultRecord.class);
        util.exportExcel(response, list, "故障记录数据");
    }

    /**
     * 获取故障记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('ops:fault:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(opsFaultRecordService.selectOpsFaultRecordById(id));
    }

    /**
     * 新增故障记录
     */
    @PreAuthorize("@ss.hasPermi('ops:fault:add')")
    @Log(title = "故障记录", businessType = BusinessType.INSERT)
    @PostMapping(value = "/add")
    public AjaxResult add(@RequestBody OpsFaultRecord opsFaultRecord)
    {
        return toAjax(opsFaultRecordService.insertOpsFaultRecord(opsFaultRecord));
    }

    /**
     * 修改故障记录
     */
    @PreAuthorize("@ss.hasPermi('ops:fault:edit')")
    @Log(title = "故障记录", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/edit")
    public AjaxResult edit(@RequestBody OpsFaultRecord opsFaultRecord)
    {
        return toAjax(opsFaultRecordService.updateOpsFaultRecord(opsFaultRecord));
    }

    /**
     * 删除故障记录
     */
    @PreAuthorize("@ss.hasPermi('ops:fault:remove')")
    @Log(title = "故障记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(opsFaultRecordService.deleteOpsFaultRecordByIds(ids));
    }
}
