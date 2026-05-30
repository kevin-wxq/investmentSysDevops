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
import com.ruoyi.devops.domain.HtRequirement;
import com.ruoyi.devops.domain.OpsIssue;
import com.ruoyi.devops.service.IOpsIssueService;

@RestController
@RequestMapping("/ops/issue")
public class OpsIssueController extends BaseController {
    @Autowired
    private IOpsIssueService opsIssueService;

    @PreAuthorize("@ss.hasPermi('ops:issue:list')")
    @GetMapping("/list")
    public TableDataInfo list(OpsIssue opsIssue) {
        startPage();
        List<OpsIssue> list = opsIssueService.selectOpsIssueList(opsIssue);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('ops:issue:export')")
    @Log(title = "运维记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OpsIssue opsIssue) {
        List<OpsIssue> list = opsIssueService.selectOpsIssueList(opsIssue);
        ExcelUtil<OpsIssue> util = new ExcelUtil<OpsIssue>(OpsIssue.class);
        util.exportExcel(response, list, "运维记录数据");
    }

    @PreAuthorize("@ss.hasPermi('ops:issue:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(opsIssueService.selectOpsIssueById(id));
    }

    @PreAuthorize("@ss.hasPermi('ops:issue:query')")
    @GetMapping("/next-issue-no")
    public AjaxResult nextIssueNo() {
        return AjaxResult.success("操作成功", opsIssueService.generateIssueNo());
    }

    @PreAuthorize("@ss.hasPermi('ops:issue:add')")
    @Log(title = "运维记录", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody OpsIssue opsIssue) {
        return toAjax(opsIssueService.insertOpsIssue(opsIssue));
    }

    @PreAuthorize("@ss.hasPermi('ops:issue:edit')")
    @Log(title = "运维记录", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody OpsIssue opsIssue) {
        return toAjax(opsIssueService.updateOpsIssue(opsIssue));
    }

    @PreAuthorize("@ss.hasPermi('ops:issue:remove')")
    @Log(title = "运维记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(opsIssueService.deleteOpsIssueByIds(ids));
    }

    @PreAuthorize("@ss.hasPermi('ops:issue:convert')")
    @Log(title = "运维记录转Bug", businessType = BusinessType.INSERT)
    @PostMapping("/{id}/convert/bug")
    public AjaxResult convertToBug(@PathVariable("id") Long id) {
        HtBugRecord bug = opsIssueService.convertToBug(id);
        return bug == null ? AjaxResult.error("运维记录不存在") : success(bug);
    }

    @PreAuthorize("@ss.hasPermi('ops:issue:convert')")
    @Log(title = "运维记录转需求", businessType = BusinessType.INSERT)
    @PostMapping("/{id}/convert/requirement")
    public AjaxResult convertToRequirement(@PathVariable("id") Long id) {
        HtRequirement requirement = opsIssueService.convertToRequirement(id);
        return requirement == null ? AjaxResult.error("运维记录不存在") : success(requirement);
    }
}
