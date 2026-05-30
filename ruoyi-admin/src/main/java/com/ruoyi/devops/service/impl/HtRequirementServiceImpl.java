package com.ruoyi.devops.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.devops.domain.HtRequirement;
import com.ruoyi.devops.domain.OpsWorkItem;
import com.ruoyi.devops.mapper.HtRequirementMapper;
import com.ruoyi.devops.service.IHtRequirementService;
import com.ruoyi.devops.service.IOpsWorkItemService;

@Service
public class HtRequirementServiceImpl implements IHtRequirementService {
    @Autowired
    private HtRequirementMapper htRequirementMapper;

    @Autowired
    private IOpsWorkItemService opsWorkItemService;

    @Override
    public HtRequirement selectHtRequirementById(Long id) {
        return htRequirementMapper.selectHtRequirementById(id);
    }

    @Override
    public List<HtRequirement> selectHtRequirementList(HtRequirement htRequirement) {
        return htRequirementMapper.selectHtRequirementList(htRequirement);
    }

    @Override
    public synchronized String generateReqNo(String deptCode, Date submitDate) {
        String dept = StringUtils.isEmpty(deptCode) ? "IT" : deptCode;
        Date date = submitDate == null ? DateUtils.getNowDate() : submitDate;
        String prefix = dept + "-REQ-" + new SimpleDateFormat("yyyyMMdd").format(date);
        String maxNo = htRequirementMapper.selectMaxReqNoByPrefix(prefix);
        return String.format("%s-%03d", prefix, parseSequence(maxNo) + 1);
    }

    @Override
    public synchronized int insertHtRequirement(HtRequirement htRequirement) {
        prepare(htRequirement);
        htRequirement.setReqNo(generateReqNo(htRequirement.getDeptCode(), htRequirement.getSubmitTime()));
        htRequirement.setCreateTime(DateUtils.getNowDate());
        int rows = htRequirementMapper.insertHtRequirement(htRequirement);
        OpsWorkItem item = buildWorkItem(htRequirement);
        item.setSourceModule("HT_REQUIREMENT");
        item.setSourceId(htRequirement.getId());
        OpsWorkItem saved = opsWorkItemService.createFromSource(item, "衡泰需求创建");
        htRequirement.setWorkItemId(saved.getId());
        htRequirementMapper.updateHtRequirement(htRequirement);
        return rows;
    }

    @Override
    public int updateHtRequirement(HtRequirement htRequirement) {
        prepare(htRequirement);
        htRequirement.setUpdateTime(DateUtils.getNowDate());
        int rows = htRequirementMapper.updateHtRequirement(htRequirement);
        HtRequirement latest = htRequirementMapper.selectHtRequirementById(htRequirement.getId());
        opsWorkItemService.syncFromSource("HT_REQUIREMENT", latest.getId(), buildWorkItem(latest), "衡泰需求同步");
        return rows;
    }

    @Override
    public int deleteHtRequirementByIds(Long[] ids) {
        return htRequirementMapper.deleteHtRequirementByIds(ids);
    }

    @Override
    public int deleteHtRequirementById(Long id) {
        return htRequirementMapper.deleteHtRequirementById(id);
    }

    private void prepare(HtRequirement requirement) {
        if (StringUtils.isEmpty(requirement.getDeptCode())) {
            requirement.setDeptCode("FI");
        }
        if (StringUtils.isEmpty(requirement.getPriority())) {
            requirement.setPriority("P2");
        }
        if (StringUtils.isEmpty(requirement.getStatus())) {
            requirement.setStatus("WAIT_ANALYSIS");
        }
        if (StringUtils.isEmpty(requirement.getBusinessFlag())) {
            requirement.setBusinessFlag("0");
        }
        if (requirement.getSubmitTime() == null) {
            requirement.setSubmitTime(DateUtils.getNowDate());
        }
        if (StringUtils.isEmpty(requirement.getDelFlag())) {
            requirement.setDelFlag("0");
        }
    }

    private OpsWorkItem buildWorkItem(HtRequirement requirement) {
        OpsWorkItem item = new OpsWorkItem();
        item.setItemType("REQ");
        item.setSystemId(requirement.getSystemId());
        item.setSystemName(requirement.getSystemName());
        item.setTitle(requirement.getReqName());
        item.setPriority(requirement.getPriority());
        item.setStatus(mapReqStatus(requirement.getStatus()));
        item.setSubmitter(requirement.getSubmitter());
        item.setSubmitTime(requirement.getSubmitTime());
        item.setPlanFinishTime(requirement.getExpectedOnlineTime());
        item.setActualFinishTime(requirement.getOnlineTime());
        item.setProgress(requirement.getProgress());
        item.setAcceptanceResult(requirement.getAcceptanceResult());
        item.setCloseDesc(requirement.getRemark());
        return item;
    }

    private String mapReqStatus(String status) {
        if ("ONLINE".equals(status)) {
            return "CLOSED";
        }
        if ("REJECTED".equals(status)) {
            return "REJECTED";
        }
        if ("WAIT_ACCEPT".equals(status)) {
            return "ACCEPTING";
        }
        if ("WAIT_ANALYSIS".equals(status)) {
            return "PENDING";
        }
        return "PROCESSING";
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
