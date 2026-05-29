package com.ruoyi.devops.service.impl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.devops.domain.OpsSystemAsset;
import com.ruoyi.devops.mapper.OpsSystemAssetMapper;
import com.ruoyi.devops.service.IOpsSystemAssetService;
@Service
public class OpsSystemAssetServiceImpl implements IOpsSystemAssetService {
    @Autowired
    private OpsSystemAssetMapper systemAssetMapper;
    @Override public List<OpsSystemAsset> selectOpsSystemAssetList(OpsSystemAsset systemAsset) { return systemAssetMapper.selectOpsSystemAssetList(systemAsset); }
    @Override public OpsSystemAsset selectOpsSystemAssetById(Long id) { return systemAssetMapper.selectOpsSystemAssetById(id); }
    @Override
    public String generateSystemCode(String systemType) {
        String prefix = getSystemCodePrefix(systemType);
        String maxCode = systemAssetMapper.selectMaxSystemCodeByPrefix(prefix);
        int next = parseSequence(maxCode) + 1;
        return String.format("%s-%04d", prefix, next);
    }
    @Override public synchronized int insertOpsSystemAsset(OpsSystemAsset systemAsset) {
        if (StringUtils.isEmpty(systemAsset.getSystemType())) {
            systemAsset.setSystemType("1");
        }
        systemAsset.setSystemCode(generateSystemCode(systemAsset.getSystemType()));
        if (StringUtils.isEmpty(systemAsset.getSystemStatus())) {
            systemAsset.setSystemStatus("1");
        }
        if (systemAsset.getOrderNum() == null) {
            systemAsset.setOrderNum(0);
        }
        systemAsset.setCreateTime(DateUtils.getNowDate());
        return systemAssetMapper.insertOpsSystemAsset(systemAsset);
    }
    @Override public int updateOpsSystemAsset(OpsSystemAsset systemAsset) {
        systemAsset.setUpdateTime(DateUtils.getNowDate());
        return systemAssetMapper.updateOpsSystemAsset(systemAsset);
    }
    @Override public int deleteOpsSystemAssetByIds(Long[] ids) { return systemAssetMapper.deleteOpsSystemAssetByIds(ids); }

    private String getSystemCodePrefix(String systemType) {
        if ("1".equals(systemType)) {
            return "CORE";
        }
        if ("2".equals(systemType)) {
            return "SUP";
        }
        if ("3".equals(systemType)) {
            return "INF";
        }
        return "SYS";
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
