package com.ruoyi.devops.mapper;

import java.util.List;
import com.ruoyi.devops.domain.OpsItemRelation;

/**
 * 事项关系Mapper接口
 *
 * @author ruoyi
 * @date 2026-05-30
 */
public interface OpsItemRelationMapper
{
    public OpsItemRelation selectOpsItemRelationById(Long id);

    public List<OpsItemRelation> selectOpsItemRelationList(OpsItemRelation opsItemRelation);

    public int insertOpsItemRelation(OpsItemRelation opsItemRelation);

    public int updateOpsItemRelation(OpsItemRelation opsItemRelation);

    public int deleteOpsItemRelationById(Long id);

    public int deleteOpsItemRelationByIds(Long[] ids);
}
