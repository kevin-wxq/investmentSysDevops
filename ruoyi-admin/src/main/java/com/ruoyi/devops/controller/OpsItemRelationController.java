package com.ruoyi.devops.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.devops.domain.OpsItemRelation;
import com.ruoyi.devops.service.IOpsItemRelationService;

@RestController
@RequestMapping("/ops/item-relation")
public class OpsItemRelationController extends BaseController {
    @Autowired
    private IOpsItemRelationService opsItemRelationService;

    @PreAuthorize("@ss.hasPermi('ops:item-relation:list')")
    @GetMapping("/list")
    public TableDataInfo list(OpsItemRelation opsItemRelation) {
        startPage();
        List<OpsItemRelation> list = opsItemRelationService.selectOpsItemRelationList(opsItemRelation);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('ops:item-relation:export')")
    @Log(title = "事项关系", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OpsItemRelation opsItemRelation) {
        List<OpsItemRelation> list = opsItemRelationService.selectOpsItemRelationList(opsItemRelation);
        ExcelUtil<OpsItemRelation> util = new ExcelUtil<OpsItemRelation>(OpsItemRelation.class);
        util.exportExcel(response, list, "事项关系数据");
    }

    @PreAuthorize("@ss.hasPermi('ops:item-relation:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(opsItemRelationService.selectOpsItemRelationById(id));
    }

    @PreAuthorize("@ss.hasPermi('ops:item-relation:add')")
    @Log(title = "事项关系", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody OpsItemRelation opsItemRelation) {
        return toAjax(opsItemRelationService.insertOpsItemRelation(opsItemRelation));
    }

    @PreAuthorize("@ss.hasPermi('ops:item-relation:edit')")
    @Log(title = "事项关系", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody OpsItemRelation opsItemRelation) {
        return toAjax(opsItemRelationService.updateOpsItemRelation(opsItemRelation));
    }

    @PreAuthorize("@ss.hasPermi('ops:item-relation:remove')")
    @Log(title = "事项关系", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(opsItemRelationService.deleteOpsItemRelationByIds(ids));
    }
}
