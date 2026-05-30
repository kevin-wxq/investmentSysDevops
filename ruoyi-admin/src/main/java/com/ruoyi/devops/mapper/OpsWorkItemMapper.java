package com.ruoyi.devops.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.devops.domain.OpsWorkItem;

/**
 * 统一闭环事项Mapper接口
 *
 * @author ruoyi
 * @date 2026-05-30
 */
public interface OpsWorkItemMapper
{
    public OpsWorkItem selectOpsWorkItemById(Long id);

    public OpsWorkItem selectOpsWorkItemBySource(@Param("sourceModule") String sourceModule, @Param("sourceId") Long sourceId);

    public List<OpsWorkItem> selectOpsWorkItemList(OpsWorkItem opsWorkItem);

    public String selectMaxItemNoByPrefix(@Param("prefix") String prefix);

    public int insertOpsWorkItem(OpsWorkItem opsWorkItem);

    public int updateOpsWorkItem(OpsWorkItem opsWorkItem);

    public int deleteOpsWorkItemById(Long id);

    public int deleteOpsWorkItemByIds(Long[] ids);

    public int countAll();

    public int countUnclosed();

    public int countClosed();

    public int countOverdue();

    public int countP0P1();

    public int countByItemType(@Param("itemType") String itemType);
}
