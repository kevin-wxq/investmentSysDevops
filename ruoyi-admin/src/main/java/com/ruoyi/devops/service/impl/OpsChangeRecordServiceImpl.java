package com.ruoyi.devops.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.devops.domain.OpsChangeRecord;
import com.ruoyi.devops.domain.OpsItemRelation;
import com.ruoyi.devops.domain.OpsWorkItem;
import com.ruoyi.devops.mapper.OpsChangeRecordMapper;
import com.ruoyi.devops.service.IOpsChangeRecordService;
import com.ruoyi.devops.service.IOpsItemRelationService;
import com.ruoyi.devops.service.IOpsWorkItemService;

@Service
public class OpsChangeRecordServiceImpl implements IOpsChangeRecordService
{
    @Autowired
    private OpsChangeRecordMapper opsChangeRecordMapper;

    @Autowired
    private IOpsWorkItemService opsWorkItemService;

    @Autowired
    private IOpsItemRelationService opsItemRelationService;

    @Override
    public OpsChangeRecord selectOpsChangeRecordById(Long id)
    {
        return opsChangeRecordMapper.selectOpsChangeRecordById(id);
    }

    @Override
    public List<OpsChangeRecord> selectOpsChangeRecordList(OpsChangeRecord opsChangeRecord)
    {
        return opsChangeRecordMapper.selectOpsChangeRecordList(opsChangeRecord);
    }

    @Override
    @Transactional
    public synchronized int insertOpsChangeRecord(OpsChangeRecord opsChangeRecord)
    {
        prepare(opsChangeRecord);
        if (StringUtils.isEmpty(opsChangeRecord.getChangeNo())) {
            opsChangeRecord.setChangeNo(generateChangeNo());
        }
        opsChangeRecord.setCreateTime(DateUtils.getNowDate());
        int rows = opsChangeRecordMapper.insertOpsChangeRecord(opsChangeRecord);
        // T7: 创建 WorkItem
        OpsWorkItem item = buildWorkItem(opsChangeRecord);
        item.setSourceModule("OPS_CHANGE");
        item.setSourceId(opsChangeRecord.getId());
        OpsWorkItem saved = opsWorkItemService.createFromSource(item, "变更记录创建");
        opsChangeRecord.setWorkItemId(saved.getId());
        opsChangeRecordMapper.updateOpsChangeRecord(opsChangeRecord);
        return rows;
    }

    @Override
    @Transactional
    public int updateOpsChangeRecord(OpsChangeRecord opsChangeRecord)
    {
        prepare(opsChangeRecord);
        opsChangeRecord.setUpdateTime(DateUtils.getNowDate());
        int rows = opsChangeRecordMapper.updateOpsChangeRecord(opsChangeRecord);
        // T7: 同步 WorkItem
        OpsChangeRecord latest = opsChangeRecordMapper.selectOpsChangeRecordById(opsChangeRecord.getId());
        if (latest != null) {
            opsWorkItemService.syncFromSource("OPS_CHANGE", latest.getId(), buildWorkItem(latest), "变更记录同步");
        }
        return rows;
    }

    @Override
    public int deleteOpsChangeRecordByIds(Long[] ids)
    {
        return opsChangeRecordMapper.deleteOpsChangeRecordByIds(ids);
    }

    @Override
    public int deleteOpsChangeRecordById(Long id)
    {
        return opsChangeRecordMapper.deleteOpsChangeRecordById(id);
    }

    @Override
    public synchronized String generateChangeNo()
    {
        String prefix = "CHG-" + new SimpleDateFormat("yyyyMMdd").format(new Date());
        String maxNo = opsChangeRecordMapper.selectMaxChangeNoByPrefix(prefix);
        return String.format("%s-%03d", prefix, parseSequence(maxNo) + 1);
    }

    private void prepare(OpsChangeRecord record) {
        if (StringUtils.isEmpty(record.getChangeType())) {
            record.setChangeType("1");
        }
        if (StringUtils.isEmpty(record.getRiskLevel())) {
            record.setRiskLevel("1");
        }
        if (StringUtils.isEmpty(record.getStatus())) {
            record.setStatus("APPLYING");
        }
        if (StringUtils.isEmpty(record.getDelFlag())) {
            record.setDelFlag("0");
        }
    }

    private OpsWorkItem buildWorkItem(OpsChangeRecord record) {
        OpsWorkItem item = new OpsWorkItem();
        item.setItemType("CHANGE");
        item.setSystemId(record.getSystemId());
        item.setSystemName(record.getSystemName());
        item.setTitle(record.getChangeTitle());
        item.setPriority(mapRiskToPriority(record.getRiskLevel()));
        item.setStatus(mapChangeStatus(record.getStatus()));
        item.setSubmitTime(record.getCreateTime());
        item.setPlanFinishTime(record.getChangeTime());
        return item;
    }

    private String mapRiskToPriority(String risk) {
        if ("3".equals(risk)) return "P0";
        if ("2".equals(risk)) return "P1";
        return "P2";
    }

    private String mapChangeStatus(String status) {
        if ("ARCHIVED".equals(status)) return "CLOSED";
        if ("REJECTED".equals(status)) return "REJECTED";
        if ("APPLYING".equals(status) || "WAIT_APPROVE".equals(status)) return "PENDING";
        if ("WAIT_VERIFY".equals(status)) return "ACCEPTING";
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
