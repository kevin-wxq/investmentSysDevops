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
import com.ruoyi.devops.domain.OpsVendorContact;
import com.ruoyi.devops.service.IOpsVendorContactService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
@RestController
@RequestMapping("/ops/vendor-contact")
public class OpsVendorContactController extends BaseController {
    @Autowired private IOpsVendorContactService vendorContactService;
    @PreAuthorize("@ss.hasPermi('ops:vendor-contact:list')") @GetMapping("/list")
    public TableDataInfo list(OpsVendorContact vendorContact) { startPage(); return getDataTable(vendorContactService.selectOpsVendorContactList(vendorContact)); }
    @PreAuthorize("@ss.hasPermi('ops:vendor-contact:export')") @Log(title="供应商联系", businessType=BusinessType.EXPORT) @PostMapping("/export")
    public void export(HttpServletResponse rsp, OpsVendorContact vendorContact) { ExcelUtil<OpsVendorContact> u=new ExcelUtil<>(OpsVendorContact.class); u.exportExcel(rsp,vendorContactService.selectOpsVendorContactList(vendorContact),"供应商联系数据"); }
    @PreAuthorize("@ss.hasPermi('ops:vendor-contact:query')") @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) { return success(vendorContactService.selectOpsVendorContactById(id)); }
    @PreAuthorize("@ss.hasPermi('ops:vendor-contact:add')") @Log(title="供应商联系", businessType=BusinessType.INSERT) @PostMapping("/add")
    public AjaxResult add(@RequestBody OpsVendorContact vendorContact) { return toAjax(vendorContactService.insertOpsVendorContact(vendorContact)); }
    @PreAuthorize("@ss.hasPermi('ops:vendor-contact:edit')") @Log(title="供应商联系", businessType=BusinessType.UPDATE) @PutMapping("/edit")
    public AjaxResult edit(@RequestBody OpsVendorContact vendorContact) { return toAjax(vendorContactService.updateOpsVendorContact(vendorContact)); }
    @PreAuthorize("@ss.hasPermi('ops:vendor-contact:remove')") @Log(title="供应商联系", businessType=BusinessType.DELETE) @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) { return toAjax(vendorContactService.deleteOpsVendorContactByIds(ids)); }
}
