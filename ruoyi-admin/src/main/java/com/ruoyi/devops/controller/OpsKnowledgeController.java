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
import com.ruoyi.devops.domain.OpsKnowledge;
import com.ruoyi.devops.service.IOpsKnowledgeService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 运维知识库Controller
 * 
 * @author ruoyi
 * @date 2026-05-30
 */
@RestController
@RequestMapping("/ops/knowledge")
public class OpsKnowledgeController extends BaseController
{
    @Autowired
    private IOpsKnowledgeService opsKnowledgeService;

    /**
     * 查询运维知识库列表
     */
    @PreAuthorize("@ss.hasPermi('ops:knowledge:list')")
    @GetMapping("/list")
    public TableDataInfo list(OpsKnowledge opsKnowledge)
    {
        startPage();
        List<OpsKnowledge> list = opsKnowledgeService.selectOpsKnowledgeList(opsKnowledge);
        return getDataTable(list);
    }

    /**
     * 导出运维知识库列表
     */
    @PreAuthorize("@ss.hasPermi('ops:knowledge:export')")
    @Log(title = "运维知识库", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OpsKnowledge opsKnowledge)
    {
        List<OpsKnowledge> list = opsKnowledgeService.selectOpsKnowledgeList(opsKnowledge);
        ExcelUtil<OpsKnowledge> util = new ExcelUtil<OpsKnowledge>(OpsKnowledge.class);
        util.exportExcel(response, list, "运维知识库数据");
    }

    /**
     * 获取运维知识库详细信息
     */
    @PreAuthorize("@ss.hasPermi('ops:knowledge:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(opsKnowledgeService.selectOpsKnowledgeById(id));
    }

    /**
     * 新增运维知识库
     */
    @PreAuthorize("@ss.hasPermi('ops:knowledge:add')")
    @Log(title = "运维知识库", businessType = BusinessType.INSERT)
    @PostMapping(value = "/add")
    public AjaxResult add(@RequestBody OpsKnowledge opsKnowledge)
    {
        return toAjax(opsKnowledgeService.insertOpsKnowledge(opsKnowledge));
    }

    /**
     * 修改运维知识库
     */
    @PreAuthorize("@ss.hasPermi('ops:knowledge:edit')")
    @Log(title = "运维知识库", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/edit")
    public AjaxResult edit(@RequestBody OpsKnowledge opsKnowledge)
    {
        return toAjax(opsKnowledgeService.updateOpsKnowledge(opsKnowledge));
    }

    /**
     * 删除运维知识库
     */
    @PreAuthorize("@ss.hasPermi('ops:knowledge:remove')")
    @Log(title = "运维知识库", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(opsKnowledgeService.deleteOpsKnowledgeByIds(ids));
    }

    @PreAuthorize("@ss.hasPermi('ops:knowledge:query')")
    @PutMapping("/{id}/view")
    public AjaxResult incrementView(@PathVariable Long id)
    {
        return toAjax(opsKnowledgeService.incrementViewCount(id));
    }
}
