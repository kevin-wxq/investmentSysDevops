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
import com.ruoyi.devops.domain.OpsTeamMember;
import com.ruoyi.devops.service.IOpsTeamMemberService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
@RestController
@RequestMapping("/ops/team-member")
public class OpsTeamMemberController extends BaseController {
    @Autowired private IOpsTeamMemberService teamMemberService;
    @PreAuthorize("@ss.hasPermi('ops:team-member:list')") @GetMapping("/list")
    public TableDataInfo list(OpsTeamMember teamMember) { startPage(); return getDataTable(teamMemberService.selectOpsTeamMemberList(teamMember)); }
    @PreAuthorize("@ss.hasPermi('ops:team-member:export')") @Log(title="团队成员", businessType=BusinessType.EXPORT) @PostMapping("/export")
    public void export(HttpServletResponse rsp, OpsTeamMember teamMember) { ExcelUtil<OpsTeamMember> u=new ExcelUtil<>(OpsTeamMember.class); u.exportExcel(rsp,teamMemberService.selectOpsTeamMemberList(teamMember),"团队成员数据"); }
    @PreAuthorize("@ss.hasPermi('ops:team-member:query')") @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) { return success(teamMemberService.selectOpsTeamMemberById(id)); }
    @PreAuthorize("@ss.hasPermi('ops:team-member:query')") @GetMapping("/next-employee-no")
    public AjaxResult nextEmployeeNo() { return AjaxResult.success("操作成功", teamMemberService.generateEmployeeNo()); }
    @PreAuthorize("@ss.hasPermi('ops:team-member:add')") @Log(title="团队成员", businessType=BusinessType.INSERT) @PostMapping("/add")
    public AjaxResult add(@RequestBody OpsTeamMember teamMember) { return toAjax(teamMemberService.insertOpsTeamMember(teamMember)); }
    @PreAuthorize("@ss.hasPermi('ops:team-member:edit')") @Log(title="团队成员", businessType=BusinessType.UPDATE) @PutMapping("/edit")
    public AjaxResult edit(@RequestBody OpsTeamMember teamMember) { return toAjax(teamMemberService.updateOpsTeamMember(teamMember)); }
    @PreAuthorize("@ss.hasPermi('ops:team-member:remove')") @Log(title="团队成员", businessType=BusinessType.DELETE) @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) { return toAjax(teamMemberService.deleteOpsTeamMemberByIds(ids)); }
}
