package com.ruoyi.devops.service;
import java.util.List;
import com.ruoyi.devops.domain.OpsSystemAsset;
public interface IOpsSystemAssetService {
    public List<OpsSystemAsset> selectOpsSystemAssetList(OpsSystemAsset systemAsset);
    public OpsSystemAsset selectOpsSystemAssetById(Long id);
    public String generateSystemCode(String systemType);
    public int insertOpsSystemAsset(OpsSystemAsset systemAsset);
    public int updateOpsSystemAsset(OpsSystemAsset systemAsset);
    public int deleteOpsSystemAssetByIds(Long[] ids);
    public java.util.Map<String, Object> getLinkedItemsCount(Long systemId);
}
