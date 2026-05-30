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
import com.ruoyi.devops.domain.HtBugRecord;
import com.ruoyi.devops.service.IHtBugRecordService;

@RestController
@RequestMapping("/ht/bug")
public class HtBugRecordController extends BaseController {
    @Autowired
    private IHtBugRecordService htBugRecordService;

    @PreAuthorize("@ss.hasPermi('ht:bug:list')")
    @GetMapping("/list")
    public TableDataInfo list(HtBugRecord htBugRecord) {
        startPage();
        List<HtBugRecord> list = htBugRecordService.selectHtBugRecordList(htBugRecord);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('ht:bug:export')")
    @Log(title = "Bug记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HtBugRecord htBugRecord) {
        List<HtBugRecord> list = htBugRecordService.selectHtBugRecordList(htBugRecord);
        ExcelUtil<HtBugRecord> util = new ExcelUtil<HtBugRecord>(HtBugRecord.class);
        util.exportExcel(response, list, "Bug记录数据");
    }

    @PreAuthorize("@ss.hasPermi('ht:bug:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(htBugRecordService.selectHtBugRecordById(id));
    }

    @PreAuthorize("@ss.hasPermi('ht:bug:query')")
    @GetMapping("/next-bug-no")
    public AjaxResult nextBugNo() {
        return AjaxResult.success("操作成功", htBugRecordService.generateBugNo());
    }

    @PreAuthorize("@ss.hasPermi('ht:bug:add')")
    @Log(title = "Bug记录", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody HtBugRecord htBugRecord) {
        return toAjax(htBugRecordService.insertHtBugRecord(htBugRecord));
    }

    @PreAuthorize("@ss.hasPermi('ht:bug:edit')")
    @Log(title = "Bug记录", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody HtBugRecord htBugRecord) {
        return toAjax(htBugRecordService.updateHtBugRecord(htBugRecord));
    }

    @PreAuthorize("@ss.hasPermi('ht:bug:remove')")
    @Log(title = "Bug记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(htBugRecordService.deleteHtBugRecordByIds(ids));
    }
}
