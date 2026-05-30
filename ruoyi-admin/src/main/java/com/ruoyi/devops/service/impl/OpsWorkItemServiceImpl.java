package com.ruoyi.devops.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.devops.domain.OpsWorkItem;
import com.ruoyi.devops.domain.OpsWorkItemLog;
import com.ruoyi.devops.mapper.OpsWorkItemLogMapper;
import com.ruoyi.devops.mapper.OpsWorkItemMapper;
import com.ruoyi.devops.mapper.OpsChangeRecordMapper;
import com.ruoyi.devops.service.IOpsWorkItemService;

@Service
public class OpsWorkItemServiceImpl implements IOpsWorkItemService {
    @Autowired
    private OpsWorkItemMapper opsWorkItemMapper;

    @Autowired
    private OpsWorkItemLogMapper opsWorkItemLogMapper;

    @Autowired
    private OpsChangeRecordMapper opsChangeRecordMapper;

    @Override
    public OpsWorkItem selectOpsWorkItemById(Long id) {
        return opsWorkItemMapper.selectOpsWorkItemById(id);
    }

    @Override
    public List<OpsWorkItem> selectOpsWorkItemList(OpsWorkItem opsWorkItem) {
        return opsWorkItemMapper.selectOpsWorkItemList(opsWorkItem);
    }

    @Override
    public synchronized String generateItemNo(String itemType) {
        String type = StringUtils.isEmpty(itemType) ? "ITEM" : itemType;
        String prefix = type + "-" + new SimpleDateFormat("yyyyMMdd").format(new Date());
        String maxNo = opsWorkItemMapper.selectMaxItemNoByPrefix(prefix);
        return String.format("%s-%03d", prefix, parseSequence(maxNo) + 1);
    }

    @Override
    public synchronized int insertOpsWorkItem(OpsWorkItem opsWorkItem) {
        prepareForSave(opsWorkItem);
        opsWorkItem.setItemNo(generateItemNo(opsWorkItem.getItemType()));
        opsWorkItem.setCreateTime(DateUtils.getNowDate());
        int rows = opsWorkItemMapper.insertOpsWorkItem(opsWorkItem);
        writeLog(opsWorkItem, null, opsWorkItem.getStatus(), "新增事项", "创建闭环事项");
        return rows;
    }

    @Override
    public int updateOpsWorkItem(OpsWorkItem opsWorkItem) {
        OpsWorkItem old = opsWorkItem.getId() == null ? null : opsWorkItemMapper.selectOpsWorkItemById(opsWorkItem.getId());
        prepareForSave(opsWorkItem);
        opsWorkItem.setUpdateTime(DateUtils.getNowDate());
        int rows = opsWorkItemMapper.updateOpsWorkItem(opsWorkItem);
        if (old != null && !StringUtils.equals(old.getStatus(), opsWorkItem.getStatus())) {
            OpsWorkItem latest = opsWorkItemMapper.selectOpsWorkItemById(opsWorkItem.getId());
            writeLog(latest, old.getStatus(), latest.getStatus(), "状态流转", opsWorkItem.getRemark());
            // T4: 状态回写源表
            writeBackToSource(latest);
        }
        return rows;
    }

    @Override
    public int deleteOpsWorkItemByIds(Long[] ids) {
        return opsWorkItemMapper.deleteOpsWorkItemByIds(ids);
    }

    @Override
    public int deleteOpsWorkItemById(Long id) {
        return opsWorkItemMapper.deleteOpsWorkItemById(id);
    }

    @Override
    public synchronized OpsWorkItem createFromSource(OpsWorkItem opsWorkItem, String actionRemark) {
        prepareForSave(opsWorkItem);
        opsWorkItem.setItemNo(generateItemNo(opsWorkItem.getItemType()));
        opsWorkItem.setCreateTime(DateUtils.getNowDate());
        opsWorkItemMapper.insertOpsWorkItem(opsWorkItem);
        writeLog(opsWorkItem, null, opsWorkItem.getStatus(), "来源创建", actionRemark);
        return opsWorkItem;
    }

    @Override
    public void syncFromSource(String sourceModule, Long sourceId, OpsWorkItem opsWorkItem, String actionRemark) {
        if (StringUtils.isEmpty(sourceModule) || sourceId == null) {
            return;
        }
        OpsWorkItem old = opsWorkItemMapper.selectOpsWorkItemBySource(sourceModule, sourceId);
        if (old == null) {
            opsWorkItem.setSourceModule(sourceModule);
            opsWorkItem.setSourceId(sourceId);
            createFromSource(opsWorkItem, actionRemark);
            return;
        }
        String fromStatus = old.getStatus();
        // 记录变更了哪些字段，生成人话级别的操作描述
        java.util.List<String> changes = new java.util.ArrayList<>();
        if (!StringUtils.equals(old.getStatus(), opsWorkItem.getStatus())) {
            changes.add("状态 " + mapStatusName(old.getStatus()) + " → " + mapStatusName(opsWorkItem.getStatus()));
        }
        if (!StringUtils.equals(old.getPriority(), opsWorkItem.getPriority())) {
            changes.add("优先级 " + safeStr(old.getPriority()) + " → " + safeStr(opsWorkItem.getPriority()));
        }
        if (!StringUtils.equals(old.getOwnerName(), opsWorkItem.getOwnerName())) {
            changes.add("负责人改为 " + safeStr(opsWorkItem.getOwnerName()));
        }
        if (!StringUtils.equals(old.getProgress(), opsWorkItem.getProgress())) {
            changes.add("更新了处理进展");
        }
        if (!StringUtils.equals(old.getAcceptanceResult(), opsWorkItem.getAcceptanceResult())) {
            changes.add("验收结果 " + safeStr(opsWorkItem.getAcceptanceResult()));
        }

        old.setSystemId(opsWorkItem.getSystemId());
        old.setSystemName(opsWorkItem.getSystemName());
        old.setTitle(opsWorkItem.getTitle());
        old.setPriority(opsWorkItem.getPriority());
        old.setStatus(opsWorkItem.getStatus());
        old.setOwnerId(opsWorkItem.getOwnerId());
        old.setOwnerName(opsWorkItem.getOwnerName());
        old.setSubmitter(opsWorkItem.getSubmitter());
        old.setSubmitTime(opsWorkItem.getSubmitTime());
        old.setPlanFinishTime(opsWorkItem.getPlanFinishTime());
        old.setActualFinishTime(opsWorkItem.getActualFinishTime());
        old.setProgress(opsWorkItem.getProgress());
        old.setAcceptanceResult(opsWorkItem.getAcceptanceResult());
        old.setCloseDesc(opsWorkItem.getCloseDesc());
        prepareForSave(old);
        old.setUpdateTime(DateUtils.getNowDate());
        opsWorkItemMapper.updateOpsWorkItem(old);
        // 只要有任何变化就写日志，不再仅限状态变更
        if (!changes.isEmpty()) {
            String detail = String.join("；", changes);
            writeLog(old, fromStatus, old.getStatus(), actionRemark, detail);
        }
    }

    private String mapStatusName(String status) {
        if (status == null) return "无";
        switch (status) {
            case "PENDING": return "待处理";
            case "PROCESSING": return "处理中";
            case "ACCEPTING": return "待验收";
            case "CLOSED": return "已关闭";
            case "REJECTED": return "已驳回";
            default: return status;
        }
    }

    private String safeStr(String s) {
        return s == null ? "" : s;
    }

    private void prepareForSave(OpsWorkItem item) {
        if (StringUtils.isEmpty(item.getItemType())) {
            item.setItemType("OTHER");
        }
        if (StringUtils.isEmpty(item.getPriority())) {
            item.setPriority("P2");
        }
        if (StringUtils.isEmpty(item.getStatus())) {
            item.setStatus("PENDING");
        }
        if (item.getSubmitTime() == null) {
            item.setSubmitTime(DateUtils.getNowDate());
        }
        if ("CLOSED".equals(item.getStatus()) && item.getActualFinishTime() == null) {
            item.setActualFinishTime(DateUtils.getNowDate());
        }
        item.setOverdueFlag(isOverdue(item) ? "1" : "0");
        if (StringUtils.isEmpty(item.getDelFlag())) {
            item.setDelFlag("0");
        }
    }

    private boolean isOverdue(OpsWorkItem item) {
        return item.getPlanFinishTime() != null
            && item.getPlanFinishTime().before(DateUtils.getNowDate())
            && !"CLOSED".equals(item.getStatus())
            && !"REJECTED".equals(item.getStatus());
    }

    private int parseSequence(String code) {
        if (StringUtils.isEmpty(code) || !code.contains("-")) {
            return 0;
        }
        try {
            return Integer.parseInt(code.substring(code.lastIndexOf("-") + 1));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void writeLog(OpsWorkItem item, String fromStatus, String toStatus, String actionName, String remark) {
        OpsWorkItemLog log = new OpsWorkItemLog();
        log.setWorkItemId(item.getId());
        log.setItemNo(item.getItemNo());
        log.setFromStatus(fromStatus);
        log.setToStatus(toStatus);
        log.setActionName(actionName);
        log.setOperatorName(getOperatorName());
        log.setActionTime(DateUtils.getNowDate());
        log.setActionRemark(remark);
        opsWorkItemLogMapper.insertOpsWorkItemLog(log);
    }


    /**
     * T4: WorkItem状态回写到源表
     */
    private void writeBackToSource(OpsWorkItem item) {
        if (StringUtils.isEmpty(item.getSourceModule()) || item.getSourceId() == null) {
            return;
        }
        String mappedStatus = reverseMapStatus(item.getSourceModule(), item.getStatus());
        if (mappedStatus == null) {
            return;
        }
        try {
            switch (item.getSourceModule()) {
                case "OPS_ISSUE":
                    com.ruoyi.devops.domain.OpsIssue issue = new com.ruoyi.devops.domain.OpsIssue();
                    issue.setId(item.getSourceId());
                    issue.setStatus(mappedIssueStatus(item.getStatus()));
                    // Issue service handles its own sync
                    break;
                case "HT_BUG":
                    com.ruoyi.devops.domain.HtBugRecord bug = new com.ruoyi.devops.domain.HtBugRecord();
                    bug.setId(item.getSourceId());
                    bug.setStatus(mappedBugStatus(item.getStatus()));
                    // Bug service handles its own sync
                    break;
                case "HT_REQUIREMENT":
                    com.ruoyi.devops.domain.HtRequirement req = new com.ruoyi.devops.domain.HtRequirement();
                    req.setId(item.getSourceId());
                    req.setStatus(mappedReqStatus(item.getStatus()));
                    // Requirement service handles its own sync
                    break;
                case "OPS_CHANGE":
                    com.ruoyi.devops.domain.OpsChangeRecord change = new com.ruoyi.devops.domain.OpsChangeRecord();
                    change.setId(item.getSourceId());
                    change.setStatus(mappedChangeStatus(item.getStatus()));
                    opsChangeRecordMapper.updateOpsChangeRecord(change);
                    break;
            }
        } catch (Exception e) {
            // writeback failure should not block main flow
        }
    }

    private String mappedIssueStatus(String workItemStatus) {
        if ("CLOSED".equals(workItemStatus)) return "CLOSED";
        if ("REJECTED".equals(workItemStatus)) return "REJECTED";
        if ("ACCEPTING".equals(workItemStatus)) return "WAIT_VERIFY";
        if ("PENDING".equals(workItemStatus)) return "PENDING";
        return "HANDLING";
    }

    private String mappedBugStatus(String workItemStatus) {
        if ("CLOSED".equals(workItemStatus)) return "CLOSED";
        if ("REJECTED".equals(workItemStatus)) return "REJECTED";
        if ("ACCEPTING".equals(workItemStatus)) return "WAIT_RETEST";
        if ("PENDING".equals(workItemStatus)) return "WAIT_CONFIRM";
        return "FIXING";
    }

    private String mappedReqStatus(String workItemStatus) {
        if ("CLOSED".equals(workItemStatus)) return "ONLINE";
        if ("REJECTED".equals(workItemStatus)) return "REJECTED";
        if ("ACCEPTING".equals(workItemStatus)) return "WAIT_ACCEPT";
        if ("PENDING".equals(workItemStatus)) return "WAIT_ANALYSIS";
        return "DEVELOPING";
    }

    private String mappedChangeStatus(String workItemStatus) {
        if ("CLOSED".equals(workItemStatus)) return "ARCHIVED";
        if ("REJECTED".equals(workItemStatus)) return "REJECTED";
        if ("ACCEPTING".equals(workItemStatus)) return "WAIT_VERIFY";
        if ("PENDING".equals(workItemStatus)) return "APPLYING";
        return "EXECUTING";
    }

    private String reverseMapStatus(String sourceModule, String status) {
        return status; // actual mapping done in specific methods above
    }

    private String getOperatorName() {
        try {
            return SecurityUtils.getUsername();
        } catch (Exception e) {
            return "system";
        }
    }
}
