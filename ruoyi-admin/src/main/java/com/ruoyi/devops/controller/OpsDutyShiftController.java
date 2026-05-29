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
import com.ruoyi.devops.domain.OpsDutyShift;
import com.ruoyi.devops.service.IOpsDutyShiftService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 值班班次Controller
 * 
 * @author ruoyi
 * @date 2026-05-30
 */
@RestController
@RequestMapping("/ops/duty-shift")
public class OpsDutyShiftController extends BaseController
{
    @Autowired
    private IOpsDutyShiftService opsDutyShiftService;

    /**
     * 查询值班班次列表
     */
    @PreAuthorize("@ss.hasPermi('ops:duty-shift:list')")
    @GetMapping("/list")
    public TableDataInfo list(OpsDutyShift opsDutyShift)
    {
        startPage();
        List<OpsDutyShift> list = opsDutyShiftService.selectOpsDutyShiftList(opsDutyShift);
        return getDataTable(list);
    }

    /**
     * 导出值班班次列表
     */
    @PreAuthorize("@ss.hasPermi('ops:duty-shift:export')")
    @Log(title = "值班班次", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OpsDutyShift opsDutyShift)
    {
        List<OpsDutyShift> list = opsDutyShiftService.selectOpsDutyShiftList(opsDutyShift);
        ExcelUtil<OpsDutyShift> util = new ExcelUtil<OpsDutyShift>(OpsDutyShift.class);
        util.exportExcel(response, list, "值班班次数据");
    }

    /**
     * 获取值班班次详细信息
     */
    @PreAuthorize("@ss.hasPermi('ops:duty-shift:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(opsDutyShiftService.selectOpsDutyShiftById(id));
    }

    /**
     * 新增值班班次
     */
    @PreAuthorize("@ss.hasPermi('ops:duty-shift:add')")
    @Log(title = "值班班次", businessType = BusinessType.INSERT)
    @PostMapping(value = "/add")
    public AjaxResult add(@RequestBody OpsDutyShift opsDutyShift)
    {
        return toAjax(opsDutyShiftService.insertOpsDutyShift(opsDutyShift));
    }

    /**
     * 修改值班班次
     */
    @PreAuthorize("@ss.hasPermi('ops:duty-shift:edit')")
    @Log(title = "值班班次", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/edit")
    public AjaxResult edit(@RequestBody OpsDutyShift opsDutyShift)
    {
        return toAjax(opsDutyShiftService.updateOpsDutyShift(opsDutyShift));
    }

    /**
     * 删除值班班次
     */
    @PreAuthorize("@ss.hasPermi('ops:duty-shift:remove')")
    @Log(title = "值班班次", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(opsDutyShiftService.deleteOpsDutyShiftByIds(ids));
    }
}
