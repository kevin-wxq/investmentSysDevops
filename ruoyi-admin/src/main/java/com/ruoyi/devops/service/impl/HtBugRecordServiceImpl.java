package com.ruoyi.devops.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.devops.domain.HtBugRecord;
import com.ruoyi.devops.domain.OpsWorkItem;
import com.ruoyi.devops.mapper.HtBugRecordMapper;
import com.ruoyi.devops.service.IHtBugRecordService;
import com.ruoyi.devops.service.IOpsWorkItemService;

@Service
public class HtBugRecordServiceImpl implements IHtBugRecordService {
    @Autowired
    private HtBugRecordMapper htBugRecordMapper;

    @Autowired
    private IOpsWorkItemService opsWorkItemService;

    @Override
    public HtBugRecord selectHtBugRecordById(Long id) {
        return htBugRecordMapper.selectHtBugRecordById(id);
    }

    @Override
    public List<HtBugRecord> selectHtBugRecordList(HtBugRecord htBugRecord) {
        return htBugRecordMapper.selectHtBugRecordList(htBugRecord);
    }

    @Override
    public synchronized String generateBugNo() {
        String prefix = "BUG-" + new SimpleDateFormat("yyyyMMdd").format(new Date());
        String maxNo = htBugRecordMapper.selectMaxBugNoByPrefix(prefix);
        return String.format("%s-%03d", prefix, parseSequence(maxNo) + 1);
    }

    @Override
    public synchronized int insertHtBugRecord(HtBugRecord htBugRecord) {
        prepare(htBugRecord);
        htBugRecord.setBugNo(generateBugNo());
        htBugRecord.setCreateTime(DateUtils.getNowDate());
        int rows = htBugRecordMapper.insertHtBugRecord(htBugRecord);
        OpsWorkItem item = buildWorkItem(htBugRecord);
        item.setSourceModule("HT_BUG");
        item.setSourceId(htBugRecord.getId());
        OpsWorkItem saved = opsWorkItemService.createFromSource(item, "创建了Bug");
        htBugRecord.setWorkItemId(saved.getId());
        htBugRecordMapper.updateHtBugRecord(htBugRecord);
        return rows;
    }

    @Override
    public int updateHtBugRecord(HtBugRecord htBugRecord) {
        prepare(htBugRecord);
        htBugRecord.setUpdateTime(DateUtils.getNowDate());
        int rows = htBugRecordMapper.updateHtBugRecord(htBugRecord);
        HtBugRecord latest = htBugRecordMapper.selectHtBugRecordById(htBugRecord.getId());
        opsWorkItemService.syncFromSource("HT_BUG", latest.getId(), buildWorkItem(latest), "更新了Bug");
        return rows;
    }

    @Override
    public int deleteHtBugRecordByIds(Long[] ids) {
        return htBugRecordMapper.deleteHtBugRecordByIds(ids);
    }

    @Override
    public int deleteHtBugRecordById(Long id) {
        return htBugRecordMapper.deleteHtBugRecordById(id);
    }

    private void prepare(HtBugRecord bug) {
        if (StringUtils.isEmpty(bug.getSeverity())) {
            bug.setSeverity("S2");
        }
        if (StringUtils.isEmpty(bug.getPriority())) {
            bug.setPriority("P2");
        }
        if (StringUtils.isEmpty(bug.getStatus())) {
            bug.setStatus("WAIT_CONFIRM");
        }
        if (bug.getFoundTime() == null) {
            bug.setFoundTime(DateUtils.getNowDate());
        }
        if (StringUtils.isEmpty(bug.getDelFlag())) {
            bug.setDelFlag("0");
        }
    }

    private OpsWorkItem buildWorkItem(HtBugRecord bug) {
        OpsWorkItem item = new OpsWorkItem();
        item.setItemType("BUG");
        item.setSystemId(bug.getSystemId());
        item.setSystemName(bug.getSystemName());
        item.setTitle(bug.getBugTitle());
        item.setPriority(bug.getPriority());
        item.setStatus(mapBugStatus(bug.getStatus()));
        item.setOwnerId(bug.getOwnerId());
        item.setOwnerName(bug.getOwnerName());
        item.setSubmitter(bug.getFounder());
        item.setSubmitTime(bug.getFoundTime());
        item.setPlanFinishTime(bug.getPlanFixTime());
        item.setActualFinishTime(bug.getActualFixTime());
        item.setProgress(bug.getFixResult());
        item.setAcceptanceResult(bug.getTestResult());
        item.setCloseDesc(bug.getCloseDesc());
        return item;
    }

    private String mapBugStatus(String status) {
        if ("CLOSED".equals(status)) {
            return "CLOSED";
        }
        if ("REJECTED".equals(status)) {
            return "REJECTED";
        }
        if ("WAIT_RETEST".equals(status)) {
            return "ACCEPTING";
        }
        if ("WAIT_CONFIRM".equals(status)) {
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
