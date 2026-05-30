package com.ruoyi.devops.controller;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.devops.domain.OpsSystemAsset;
import com.ruoyi.devops.service.IOpsSystemAssetService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
@RestController
@RequestMapping("/ops/system-asset")
public class OpsSystemAssetController extends BaseController {
    @Autowired private IOpsSystemAssetService systemAssetService;
    @PreAuthorize("@ss.hasPermi('ops:system-asset:list')") @GetMapping("/list")
    public TableDataInfo list(OpsSystemAsset systemAsset) { startPage(); return getDataTable(systemAssetService.selectOpsSystemAssetList(systemAsset)); }
    @PreAuthorize("@ss.hasPermi('ops:system-asset:export')") @Log(title="系统资产", businessType=BusinessType.EXPORT) @PostMapping("/export")
    public void export(HttpServletResponse rsp, OpsSystemAsset systemAsset) { ExcelUtil<OpsSystemAsset> u=new ExcelUtil<>(OpsSystemAsset.class); u.exportExcel(rsp,systemAssetService.selectOpsSystemAssetList(systemAsset),"系统资产数据"); }
    @PreAuthorize("@ss.hasPermi('ops:system-asset:query')") @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) { return success(systemAssetService.selectOpsSystemAssetById(id)); }
    @PreAuthorize("@ss.hasPermi('ops:system-asset:query')") @GetMapping("/next-code")
    public AjaxResult nextCode(@RequestParam(required = false) String systemType) { return AjaxResult.success("操作成功", systemAssetService.generateSystemCode(systemType)); }
    @PreAuthorize("@ss.hasPermi('ops:system-asset:add')") @Log(title="系统资产", businessType=BusinessType.INSERT) @PostMapping("/add")
    public AjaxResult add(@RequestBody OpsSystemAsset systemAsset) { return toAjax(systemAssetService.insertOpsSystemAsset(systemAsset)); }
    @PreAuthorize("@ss.hasPermi('ops:system-asset:edit')") @Log(title="系统资产", businessType=BusinessType.UPDATE) @PutMapping("/edit")
    public AjaxResult edit(@RequestBody OpsSystemAsset systemAsset) { return toAjax(systemAssetService.updateOpsSystemAsset(systemAsset)); }
    @PreAuthorize("@ss.hasPermi('ops:system-asset:remove')") @Log(title="系统资产", businessType=BusinessType.DELETE) @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) { return toAjax(systemAssetService.deleteOpsSystemAssetByIds(ids)); }
    @PreAuthorize("@ss.hasPermi('ops:system-asset:query')") @GetMapping("/{id}/linked-items")
    public AjaxResult getLinkedItems(@PathVariable Long id) { return success(systemAssetService.getLinkedItemsCount(id)); }
}
