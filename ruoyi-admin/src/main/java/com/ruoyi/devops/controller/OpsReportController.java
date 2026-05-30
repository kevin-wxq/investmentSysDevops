package com.ruoyi.devops.controller;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.devops.utils.WordExportUtil;
import com.ruoyi.devops.domain.OpsWorkItem;
import com.ruoyi.devops.mapper.OpsWorkItemMapper;
import com.ruoyi.devops.service.IOpsWorkItemService;

@RestController
@RequestMapping("/ops/report")
public class OpsReportController extends BaseController {
    @Autowired
    private OpsWorkItemMapper opsWorkItemMapper;

    @Autowired
    private IOpsWorkItemService opsWorkItemService;

    @PreAuthorize("@ss.hasPermi('ops:report:view')")
    @GetMapping("/summary")
    public AjaxResult summary(OpsWorkItem opsWorkItem) {
        List<OpsWorkItem> list = opsWorkItemService.selectOpsWorkItemList(opsWorkItem);
        Map<String, Object> data = new HashMap<String, Object>();
        int total = list.size();
        int closed = 0;
        int overdue = 0;
        int p0p1 = 0;
        List<OpsWorkItem> overdueList = new ArrayList<OpsWorkItem>();
        List<OpsWorkItem> unclosedList = new ArrayList<OpsWorkItem>();
        Map<String, StatRow> typeMap = new LinkedHashMap<String, StatRow>();
        Map<String, StatRow> priorityMap = new LinkedHashMap<String, StatRow>();
        for (OpsWorkItem item : list) {
            boolean closedFlag = "CLOSED".equals(item.getStatus());
            boolean rejectedFlag = "REJECTED".equals(item.getStatus());
            if (closedFlag) {
                closed++;
            }
            if (!closedFlag && !rejectedFlag) {
                unclosedList.add(item);
            }
            if ("1".equals(item.getOverdueFlag())) {
                overdue++;
                overdueList.add(item);
            }
            if ("P0".equals(item.getPriority()) || "P1".equals(item.getPriority())) {
                p0p1++;
            }
            addStat(typeMap, item.getItemType(), closedFlag, "1".equals(item.getOverdueFlag()), item.getProgress());
            addStat(priorityMap, item.getPriority(), closedFlag, "1".equals(item.getOverdueFlag()), item.getProgress());
        }
        int unclosed = total - closed;
        data.put("total", total);
        data.put("totalItems", total);
        data.put("closed", closed);
        data.put("closedItems", closed);
        data.put("unclosed", unclosed);
        data.put("unclosedItems", unclosed);
        data.put("overdue", overdue);
        data.put("overdueItems", overdue);
        data.put("p0p1Items", p0p1);
        data.put("requirementItems", countByType(list, "REQ"));
        data.put("bugItems", countByType(list, "BUG"));
        data.put("faultItems", countByType(list, "FAULT"));
        data.put("closeRate", total == 0 ? 0 : closed * 100 / total);
        data.put("typeStats", toStatList(typeMap, "type"));
        data.put("priorityStats", toStatList(priorityMap, "priority"));
        data.put("overdueSummary", buildSummary(overdueList));
        data.put("unclosedSummary", buildSummary(unclosedList));
        data.put("allItems", buildAllItemsList(list));
        return success(data);
    }

    @PreAuthorize("@ss.hasPermi('ops:report:export')")
    @Log(title = "闭环报告", businessType = BusinessType.EXPORT)
    @PostMapping("/work-item/export")
    public void exportWorkItems(HttpServletResponse response, OpsWorkItem opsWorkItem) {
        List<OpsWorkItem> list = opsWorkItemService.selectOpsWorkItemList(opsWorkItem);
        ExcelUtil<OpsWorkItem> util = new ExcelUtil<OpsWorkItem>(OpsWorkItem.class);
        util.exportExcel(response, list, "闭环事项报告");
    }

    @PreAuthorize("@ss.hasPermi('ops:report:export')")
    @Log(title = "运维报告Word", businessType = BusinessType.EXPORT)
    @PostMapping("/work-item/export-word")
    public void exportWord(HttpServletResponse response, OpsWorkItem opsWorkItem) throws Exception {
        Map<String, Object> data = new HashMap<>();
        List<OpsWorkItem> list = opsWorkItemService.selectOpsWorkItemList(opsWorkItem);
        int total = list.size();
        int closed = 0;
        int overdue = 0;
        List<OpsWorkItem> overdueList = new ArrayList<>();
        List<OpsWorkItem> unclosedList = new ArrayList<>();
        Map<String, StatRow> typeMap = new LinkedHashMap<>();
        Map<String, StatRow> priorityMap = new LinkedHashMap<>();
        for (OpsWorkItem item : list) {
            boolean closedFlag = "CLOSED".equals(item.getStatus());
            boolean rejectedFlag = "REJECTED".equals(item.getStatus());
            if (closedFlag) closed++;
            if (!closedFlag && !rejectedFlag) unclosedList.add(item);
            if ("1".equals(item.getOverdueFlag())) { overdue++; overdueList.add(item); }
            addStat(typeMap, item.getItemType(), closedFlag, "1".equals(item.getOverdueFlag()), item.getProgress());
            addStat(priorityMap, item.getPriority(), closedFlag, "1".equals(item.getOverdueFlag()), item.getProgress());
        }
        int unclosed = total - closed;
        data.put("total", total);
        data.put("closed", closed);
        data.put("unclosed", unclosed);
        data.put("overdue", overdue);
        data.put("closeRate", total == 0 ? 0 : closed * 100 / total);
        data.put("typeStats", toStatList(typeMap, "type"));
        data.put("priorityStats", toStatList(priorityMap, "priority"));
        data.put("overdueSummary", buildSummary(overdueList));
        data.put("unclosedSummary", buildSummary(unclosedList));
        
        String beginTime = opsWorkItem.getParams() != null ? (String) opsWorkItem.getParams().get("beginTime") : null;
        String endTime = opsWorkItem.getParams() != null ? (String) opsWorkItem.getParams().get("endTime") : null;
        
        byte[] bytes = WordExportUtil.generateReport(data, beginTime, endTime);
        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        response.setHeader("Content-Disposition", "attachment; filename=ops_report.docx");
        response.getOutputStream().write(bytes);
        response.getOutputStream().flush();
    }

    private void addStat(Map<String, StatRow> map, String code, boolean closed, boolean overdue, String progress) {
        String key = code == null || "".equals(code) ? "OTHER" : code;
        StatRow row = map.get(key);
        if (row == null) {
            row = new StatRow(key);
            map.put(key, row);
        }
        row.total++;
        if (closed) {
            row.closed++;
        }
        if (overdue) {
            row.overdue++;
        }
        try {
            row.progressTotal += Integer.parseInt(progress);
            row.progressCount++;
        } catch (Exception e) {
            // Text progress is allowed; it simply does not join the average.
        }
    }

    private int countByType(List<OpsWorkItem> list, String itemType) {
        int count = 0;
        for (OpsWorkItem item : list) {
            if (itemType.equals(item.getItemType())) {
                count++;
            }
        }
        return count;
    }

    private List<Map<String, Object>> toStatList(Map<String, StatRow> statMap, String codeKey) {
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        for (StatRow stat : statMap.values()) {
            Map<String, Object> row = new HashMap<String, Object>();
            row.put(codeKey, stat.code);
            row.put("total", stat.total);
            row.put("closed", stat.closed);
            row.put("unclosed", stat.total - stat.closed);
            row.put("overdue", stat.overdue);
            row.put("closeRate", stat.total == 0 ? 0 : stat.closed * 100 / stat.total);
            rows.add(row);
        }
        return rows;
    }

    private List<Map<String, Object>> buildAllItemsList(List<OpsWorkItem> items) {
        List<Map<String, Object>> itemMaps = new ArrayList<>();
        for (OpsWorkItem item : items) {
            Map<String, Object> map = new HashMap<>();
            map.put("itemNo", item.getItemNo());
            map.put("itemType", item.getItemType());
            map.put("title", item.getTitle());
            map.put("priority", item.getPriority());
            map.put("status", item.getStatus());
            map.put("ownerName", item.getOwnerName());
            map.put("createTime", item.getCreateTime());
            map.put("planFinishTime", item.getPlanFinishTime());
            map.put("overdueFlag", item.getOverdueFlag());
            itemMaps.add(map);
        }
        return itemMaps;
    }

    private Map<String, Object> buildSummary(List<OpsWorkItem> items) {
        Map<String, Object> summary = new HashMap<String, Object>();
        summary.put("total", items.size());
        List<Map<String, Object>> itemMaps = new ArrayList<>();
        int limit = Math.min(items.size(), 10);
        for (int i = 0; i < limit; i++) {
            OpsWorkItem item = items.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put("itemNo", item.getItemNo());
            map.put("title", item.getTitle());
            map.put("status", item.getStatus());
            map.put("ownerName", item.getOwnerName());
            map.put("planFinishTime", item.getPlanFinishTime());
            itemMaps.add(map);
        }
        summary.put("items", itemMaps);
        return summary;
    }

    private static class StatRow {
        private String code;
        private int total;
        private int closed;
        private int overdue;
        private int progressTotal;
        private int progressCount;

        private StatRow(String code) {
            this.code = code;
        }
    }
}
