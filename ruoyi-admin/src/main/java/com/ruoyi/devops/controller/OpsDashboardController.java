package com.ruoyi.devops.controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.devops.mapper.*;

@RestController
@RequestMapping("/ops/dashboard")
public class OpsDashboardController {

    @Autowired(required = false) private OpsSystemAssetMapper systemAssetMapper;
    @Autowired(required = false) private OpsFaultRecordMapper faultRecordMapper;
    @Autowired(required = false) private DailyInspectionMainMapper inspectionMapper;
    @Autowired(required = false) private OpsChangeRecordMapper changeRecordMapper;

    @PreAuthorize("@ss.hasPermi('ops:dashboard:view')")
    @GetMapping("/stats")
    public AjaxResult stats() {
        Map<String, Object> data = new HashMap<>();
        try {
            data.put("systemCount", systemAssetMapper != null ? systemAssetMapper.selectOpsSystemAssetList(null).size() : 0);
        } catch(Exception e) { data.put("systemCount", 0); }
        try {
            data.put("faultCount", faultRecordMapper != null ? faultRecordMapper.selectOpsFaultRecordList(null).size() : 0);
        } catch(Exception e) { data.put("faultCount", 0); }
        data.put("inspectionRate", 95);
        data.put("pendingChanges", 3);

        // Pie data for system types
        List<Map<String,Object>> sysTypeData = new ArrayList<>();
        sysTypeData.add(mapOf("value",35,"name","核心业务"));
        sysTypeData.add(mapOf("value",25,"name","支撑系统"));
        sysTypeData.add(mapOf("value",20,"name","基础设施"));
        data.put("systemTypeData", sysTypeData);

        // Fault trend (monthly demo)
        data.put("faultTrend", Arrays.asList(5,8,6,9,7,4,3,6,5,8,4,7));
        data.put("inspectionData", Arrays.asList(85,90,78,92,88,95));
        data.put("changeTypeData", Arrays.asList(
            mapOf("value",15,"name","版本升级"),mapOf("value",25,"name","配置变更"),
            mapOf("value",20,"name","数据库变更"),mapOf("value",10,"name","网络变更"),
            mapOf("value",5,"name","其他")));

        try {
            data.put("recentFaults", faultRecordMapper != null ? faultRecordMapper.selectOpsFaultRecordList(null) : Collections.emptyList());
        } catch(Exception e) { data.put("recentFaults", Collections.emptyList()); }
        try {
            data.put("recentChanges", changeRecordMapper != null ? changeRecordMapper.selectOpsChangeRecordList(null) : Collections.emptyList());
        } catch(Exception e) { data.put("recentChanges", Collections.emptyList()); }

        return AjaxResult.success(data);
    }

    private Map<String,Object> mapOf(Object... pairs) {
        Map<String,Object> m = new HashMap<>();
        for (int i = 0; i + 1 < pairs.length; i += 2) {
            m.put(String.valueOf(pairs[i]), pairs[i + 1]);
        }
        return m;
    }
}
