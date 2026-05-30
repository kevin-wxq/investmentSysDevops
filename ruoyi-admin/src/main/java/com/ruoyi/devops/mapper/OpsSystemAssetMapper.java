package com.ruoyi.devops.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.devops.domain.OpsSystemAsset;
public interface OpsSystemAssetMapper {
    public List<OpsSystemAsset> selectOpsSystemAssetList(OpsSystemAsset systemAsset);
    public OpsSystemAsset selectOpsSystemAssetById(Long id);
    public String selectMaxSystemCodeByPrefix(@Param("prefix") String prefix);
    public int insertOpsSystemAsset(OpsSystemAsset systemAsset);
    public int updateOpsSystemAsset(OpsSystemAsset systemAsset);
    public int deleteOpsSystemAssetById(Long id);
    public int deleteOpsSystemAssetByIds(Long[] ids);
    public int countIssuesBySystemId(@Param("systemId") Long systemId);
    public int countBugsBySystemId(@Param("systemId") Long systemId);
    public int countReqsBySystemId(@Param("systemId") Long systemId);
    public int countChangesBySystemId(@Param("systemId") Long systemId);
}
