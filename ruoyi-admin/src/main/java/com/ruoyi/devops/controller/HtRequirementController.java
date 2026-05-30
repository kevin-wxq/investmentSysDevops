package com.ruoyi.devops.controller;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.devops.domain.HtRequirement;
import com.ruoyi.devops.service.IHtRequirementService;

@RestController
@RequestMapping("/ht/requirement")
public class HtRequirementController extends BaseController {
    @Autowired
    private IHtRequirementService htRequirementService;

    @PreAuthorize("@ss.hasPermi('ht:requirement:list')")
    @GetMapping("/list")
    public TableDataInfo list(HtRequirement htRequirement) {
        startPage();
        List<HtRequirement> list = htRequirementService.selectHtRequirementList(htRequirement);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('ht:requirement:export')")
    @Log(title = "衡泰需求", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HtRequirement htRequirement) {
        List<HtRequirement> list = htRequirementService.selectHtRequirementList(htRequirement);
        ExcelUtil<HtRequirement> util = new ExcelUtil<HtRequirement>(HtRequirement.class);
        util.exportExcel(response, list, "衡泰需求数据");
    }

    @PreAuthorize("@ss.hasPermi('ht:requirement:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(htRequirementService.selectHtRequirementById(id));
    }

    @PreAuthorize("@ss.hasPermi('ht:requirement:query')")
    @GetMapping("/next-req-no")
    public AjaxResult nextReqNo(@RequestParam("deptCode") String deptCode,
        @RequestParam(value = "proposeDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date proposeDate,
        @RequestParam(value = "submitDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date submitDate) {
        return AjaxResult.success("操作成功", htRequirementService.generateReqNo(deptCode, proposeDate != null ? proposeDate : submitDate));
    }

    @PreAuthorize("@ss.hasPermi('ht:requirement:add')")
    @Log(title = "衡泰需求", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody HtRequirement htRequirement) {
        return toAjax(htRequirementService.insertHtRequirement(htRequirement));
    }

    @PreAuthorize("@ss.hasPermi('ht:requirement:edit')")
    @Log(title = "衡泰需求", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody HtRequirement htRequirement) {
        return toAjax(htRequirementService.updateHtRequirement(htRequirement));
    }

    @PreAuthorize("@ss.hasPermi('ht:requirement:remove')")
    @Log(title = "衡泰需求", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(htRequirementService.deleteHtRequirementByIds(ids));
    }
}
