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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.devops.domain.OpsWorkItem;
import com.ruoyi.devops.service.IOpsWorkItemService;

@RestController
@RequestMapping("/ops/work-item")
public class OpsWorkItemController extends BaseController {
    @Autowired
    private IOpsWorkItemService opsWorkItemService;

    @PreAuthorize("@ss.hasPermi('ops:work-item:list')")
    @GetMapping("/list")
    public TableDataInfo list(OpsWorkItem opsWorkItem) {
        startPage();
        List<OpsWorkItem> list = opsWorkItemService.selectOpsWorkItemList(opsWorkItem);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('ops:work-item:export')")
    @Log(title = "闭环事项", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OpsWorkItem opsWorkItem) {
        List<OpsWorkItem> list = opsWorkItemService.selectOpsWorkItemList(opsWorkItem);
        ExcelUtil<OpsWorkItem> util = new ExcelUtil<OpsWorkItem>(OpsWorkItem.class);
        util.exportExcel(response, list, "闭环事项数据");
    }

    @PreAuthorize("@ss.hasPermi('ops:work-item:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(opsWorkItemService.selectOpsWorkItemById(id));
    }

    @PreAuthorize("@ss.hasPermi('ops:work-item:query')")
    @GetMapping("/next-item-no")
    public AjaxResult nextItemNo(@RequestParam(value = "itemType", required = false) String itemType) {
        return AjaxResult.success("操作成功", opsWorkItemService.generateItemNo(itemType));
    }

    @PreAuthorize("@ss.hasPermi('ops:work-item:add')")
    @Log(title = "闭环事项", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody OpsWorkItem opsWorkItem) {
        return toAjax(opsWorkItemService.insertOpsWorkItem(opsWorkItem));
    }

    @PreAuthorize("@ss.hasPermi('ops:work-item:edit')")
    @Log(title = "闭环事项", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody OpsWorkItem opsWorkItem) {
        return toAjax(opsWorkItemService.updateOpsWorkItem(opsWorkItem));
    }

    @PreAuthorize("@ss.hasPermi('ops:work-item:remove')")
    @Log(title = "闭环事项", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(opsWorkItemService.deleteOpsWorkItemByIds(ids));
    }
}
