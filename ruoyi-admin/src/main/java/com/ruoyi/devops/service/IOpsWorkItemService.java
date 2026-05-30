package com.ruoyi.devops.service;

import java.util.List;
import com.ruoyi.devops.domain.OpsWorkItem;

/**
 * 统一闭环事项Service接口
 *
 * @author ruoyi
 * @date 2026-05-30
 */
public interface IOpsWorkItemService
{
    public OpsWorkItem selectOpsWorkItemById(Long id);

    public List<OpsWorkItem> selectOpsWorkItemList(OpsWorkItem opsWorkItem);

    public String generateItemNo(String itemType);

    public int insertOpsWorkItem(OpsWorkItem opsWorkItem);

    public int updateOpsWorkItem(OpsWorkItem opsWorkItem);

    public int deleteOpsWorkItemByIds(Long[] ids);

    public int deleteOpsWorkItemById(Long id);

    public OpsWorkItem createFromSource(OpsWorkItem opsWorkItem, String actionRemark);

    public void syncFromSource(String sourceModule, Long sourceId, OpsWorkItem opsWorkItem, String actionRemark);
}
