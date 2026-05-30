package com.ruoyi.devops.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.devops.domain.OpsWorkItemLog;
import com.ruoyi.devops.service.IOpsWorkItemLogService;

@RestController
@RequestMapping("/ops/work-item-log")
public class OpsWorkItemLogController extends BaseController {
    @Autowired
    private IOpsWorkItemLogService opsWorkItemLogService;

    @PreAuthorize("@ss.hasPermi('ops:work-item:list')")
    @GetMapping("/list")
    public TableDataInfo list(OpsWorkItemLog opsWorkItemLog) {
        startPage();
        List<OpsWorkItemLog> list = opsWorkItemLogService.selectOpsWorkItemLogList(opsWorkItemLog);
        return getDataTable(list);
    }
}
