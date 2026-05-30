package com.ruoyi.devops.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.devops.domain.HtBugRecord;
import com.ruoyi.devops.domain.HtRequirement;
import com.ruoyi.devops.domain.OpsIssue;
import com.ruoyi.devops.domain.OpsItemRelation;
import com.ruoyi.devops.domain.OpsWorkItem;
import com.ruoyi.devops.mapper.OpsIssueMapper;
import com.ruoyi.devops.service.IHtBugRecordService;
import com.ruoyi.devops.service.IHtRequirementService;
import com.ruoyi.devops.service.IOpsIssueService;
import com.ruoyi.devops.service.IOpsItemRelationService;
import com.ruoyi.devops.service.IOpsWorkItemService;

@Service
public class OpsIssueServiceImpl implements IOpsIssueService {
    @Autowired
    private OpsIssueMapper opsIssueMapper;

    @Autowired
    private IOpsWorkItemService opsWorkItemService;

    @Autowired
    private IHtBugRecordService htBugRecordService;

    @Autowired
    private IHtRequirementService htRequirementService;

    @Autowired
    private IOpsItemRelationService opsItemRelationService;

    @Override
    public OpsIssue selectOpsIssueById(Long id) {
        return opsIssueMapper.selectOpsIssueById(id);
    }

    @Override
    public List<OpsIssue> selectOpsIssueList(OpsIssue opsIssue) {
        return opsIssueMapper.selectOpsIssueList(opsIssue);
    }

    @Override
    public synchronized String generateIssueNo() {
        String prefix = "OPS-ISSUE-" + new SimpleDateFormat("yyyyMMdd").format(new Date());
        String maxNo = opsIssueMapper.selectMaxIssueNoByPrefix(prefix);
        return String.format("%s-%03d", prefix, parseSequence(maxNo) + 1);
    }

    @Override
    @Transactional
    public synchronized int insertOpsIssue(OpsIssue opsIssue) {
        prepare(opsIssue);
        opsIssue.setIssueNo(generateIssueNo());
        opsIssue.setCreateTime(DateUtils.getNowDate());
        int rows = opsIssueMapper.insertOpsIssue(opsIssue);
        OpsWorkItem item = buildWorkItem(opsIssue);
        item.setSourceModule("OPS_ISSUE");
        item.setSourceId(opsIssue.getId());
        OpsWorkItem saved = opsWorkItemService.createFromSource(item, "创建了运维记录");
        opsIssue.setWorkItemId(saved.getId());
        opsIssueMapper.updateOpsIssue(opsIssue);
        return rows;
    }

    @Override
    @Transactional
    public int updateOpsIssue(OpsIssue opsIssue) {
        prepare(opsIssue);
        opsIssue.setUpdateTime(DateUtils.getNowDate());
        int rows = opsIssueMapper.updateOpsIssue(opsIssue);
        OpsIssue latest = opsIssueMapper.selectOpsIssueById(opsIssue.getId());
        opsWorkItemService.syncFromSource("OPS_ISSUE", latest.getId(), buildWorkItem(latest), "更新了运维记录");
        return rows;
    }

    @Override
    public int deleteOpsIssueByIds(Long[] ids) {
        return opsIssueMapper.deleteOpsIssueByIds(ids);
    }

    @Override
    public int deleteOpsIssueById(Long id) {
        return opsIssueMapper.deleteOpsIssueById(id);
    }

    @Override
    @Transactional
    public HtBugRecord convertToBug(Long id) {
        OpsIssue issue = opsIssueMapper.selectOpsIssueById(id);
        if (issue == null) {
            return null;
        }
        // T3: 幂等保护 — 已转Bug则不重复创建
        if ("CONVERTED_BUG".equals(issue.getStatus())) {
            if (issue.getRelatedBugNo() != null) {
                HtBugRecord existing = new HtBugRecord();
                existing.setBugNo(issue.getRelatedBugNo());
                List<HtBugRecord> list = htBugRecordService.selectHtBugRecordList(existing);
                if (!list.isEmpty()) {
                    return list.get(0);
                }
            }
            return null;
        }
        HtBugRecord bug = new HtBugRecord();
        bug.setRequirementNo(issue.getRelatedReqNo());
        bug.setSystemId(issue.getSystemId());
        bug.setSystemName(issue.getSystemName());
        bug.setBugTitle(issue.getIssueTitle());
        bug.setBugDesc(issue.getIssueDesc());
        bug.setSeverity("S2");
        bug.setPriority(issue.getPriority());
        bug.setStatus("WAIT_CONFIRM");
        bug.setFounder(issue.getFounder());
        bug.setFoundTime(issue.getFoundTime());
        bug.setOwnerId(issue.getOwnerId());
        bug.setOwnerName(issue.getOwnerName());
        bug.setFixPlan(issue.getHandleResult());
        bug.setRemark("由运维记录 " + issue.getIssueNo() + " 转入");
        htBugRecordService.insertHtBugRecord(bug);
        issue.setStatus("CONVERTED_BUG");
        issue.setHandleMethod("TO_BUG");
        issue.setRelatedBugNo(bug.getBugNo());
        issue.setHandleResult(appendResult(issue.getHandleResult(), "已转 Bug：" + bug.getBugNo()));
        issue.setUpdateTime(DateUtils.getNowDate());
        opsIssueMapper.updateOpsIssue(issue);
        createRelation(issue, "HT_BUG", bug.getId(), bug.getBugNo(), "GENERATE", "运维记录转 Bug");
        opsWorkItemService.syncFromSource("OPS_ISSUE", issue.getId(), buildWorkItem(issue), "转Bug");
        return bug;
    }

    @Override
    @Transactional
    public HtRequirement convertToRequirement(Long id) {
        OpsIssue issue = opsIssueMapper.selectOpsIssueById(id);
        if (issue == null) {
            return null;
        }
        // T3: 幂等保护 — 已转需求则不重复创建
        if ("CONVERTED_REQ".equals(issue.getStatus())) {
            if (issue.getRelatedReqNo() != null) {
                HtRequirement existing = new HtRequirement();
                existing.setReqNo(issue.getRelatedReqNo());
                List<HtRequirement> list = htRequirementService.selectHtRequirementList(existing);
                if (!list.isEmpty()) {
                    return list.get(0);
                }
            }
            return null;
        }
        HtRequirement requirement = new HtRequirement();
        requirement.setDeptCode("FI");
        requirement.setReqName(issue.getIssueTitle());
        requirement.setPriority(issue.getPriority());
        requirement.setModuleCode(issue.getModuleCode());
        requirement.setSystemId(issue.getSystemId());
        requirement.setSystemName(issue.getSystemName());
        requirement.setReqDesc(issue.getIssueDesc());
        requirement.setBusinessValue("由运维记录 " + issue.getIssueNo() + " 转入，建议纳入需求评估。");
        requirement.setBusinessFlag("0");
        requirement.setSubmitter(issue.getFounder());
        requirement.setSubmitTime(issue.getFoundTime());
        requirement.setStatus("WAIT_ANALYSIS");
        requirement.setProgress(issue.getHandleResult());
        requirement.setRemark("由运维记录 " + issue.getIssueNo() + " 转入");
        htRequirementService.insertHtRequirement(requirement);
        issue.setStatus("CONVERTED_REQ");
        issue.setHandleMethod("TO_REQ");
        issue.setRelatedReqNo(requirement.getReqNo());
        issue.setHandleResult(appendResult(issue.getHandleResult(), "已转需求：" + requirement.getReqNo()));
        issue.setUpdateTime(DateUtils.getNowDate());
        opsIssueMapper.updateOpsIssue(issue);
        createRelation(issue, "HT_REQUIREMENT", requirement.getId(), requirement.getReqNo(), "CONVERT_TO", "运维记录转需求");
        opsWorkItemService.syncFromSource("OPS_ISSUE", issue.getId(), buildWorkItem(issue), "转需求");
        return requirement;
    }

    private void prepare(OpsIssue issue) {
        if (StringUtils.isEmpty(issue.getIssueType())) {
            issue.setIssueType("FUNCTION");
        }
        if (StringUtils.isEmpty(issue.getPriority())) {
            issue.setPriority("P2");
        }
        if (StringUtils.isEmpty(issue.getStatus())) {
            issue.setStatus("PENDING");
        }
        if (issue.getFoundTime() == null) {
            issue.setFoundTime(DateUtils.getNowDate());
        }
        if (StringUtils.isEmpty(issue.getDelFlag())) {
            issue.setDelFlag("0");
        }
    }

    private OpsWorkItem buildWorkItem(OpsIssue issue) {
        OpsWorkItem item = new OpsWorkItem();
        item.setItemType("ISSUE");
        item.setSystemId(issue.getSystemId());
        item.setSystemName(issue.getSystemName());
        item.setTitle(issue.getIssueTitle());
        item.setPriority(issue.getPriority());
        item.setStatus(mapIssueStatus(issue.getStatus()));
        item.setOwnerId(issue.getOwnerId());
        item.setOwnerName(issue.getOwnerName());
        item.setSubmitter(issue.getFounder());
        item.setSubmitTime(issue.getFoundTime());
        item.setProgress(issue.getHandleResult());
        item.setCloseDesc(issue.getRemark());
        return item;
    }

    private String mapIssueStatus(String status) {
        if ("CLOSED".equals(status)) {
            return "CLOSED";
        }
        if ("REJECTED".equals(status)) {
            return "REJECTED";
        }
        if ("WAIT_VERIFY".equals(status)) {
            return "ACCEPTING";
        }
        if ("PENDING".equals(status)) {
            return "PENDING";
        }
        return "PROCESSING";
    }

    private void createRelation(OpsIssue issue, String targetType, Long targetId, String targetNo, String relationType, String desc) {
        OpsItemRelation relation = new OpsItemRelation();
        relation.setSourceType("OPS_ISSUE");
        relation.setSourceId(issue.getId());
        relation.setSourceNo(issue.getIssueNo());
        relation.setTargetType(targetType);
        relation.setTargetId(targetId);
        relation.setTargetNo(targetNo);
        relation.setRelationType(relationType);
        relation.setRelationDesc(desc);
        relation.setRelationTime(DateUtils.getNowDate());
        opsItemRelationService.insertOpsItemRelation(relation);
    }

    private String appendResult(String oldValue, String nextValue) {
        if (StringUtils.isEmpty(oldValue)) {
            return nextValue;
        }
        return oldValue + "\n" + nextValue;
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
}
