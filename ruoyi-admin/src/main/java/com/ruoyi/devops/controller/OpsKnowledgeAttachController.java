package com.ruoyi.devops.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.devops.domain.OpsKnowledgeAttach;
import com.ruoyi.devops.service.IOpsKnowledgeAttachService;

@RestController
@RequestMapping("/ops/knowledge-attach")
public class OpsKnowledgeAttachController extends BaseController {
    @Autowired
    private IOpsKnowledgeAttachService attachService;

    @PreAuthorize("@ss.hasPermi('ops:knowledge:list')")
    @GetMapping("/list/{knowledgeId}")
    public AjaxResult list(@PathVariable Long knowledgeId) {
        List<OpsKnowledgeAttach> list = attachService.selectOpsKnowledgeAttachByKnowledgeId(knowledgeId);
        return success(list);
    }

    @PreAuthorize("@ss.hasPermi('ops:knowledge:add')")
    @Log(title = "知识库附件", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody OpsKnowledgeAttach attach) {
        return toAjax(attachService.insertOpsKnowledgeAttach(attach));
    }

    @PreAuthorize("@ss.hasPermi('ops:knowledge:remove')")
    @Log(title = "知识库附件", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(attachService.deleteOpsKnowledgeAttachById(id));
    }
}
