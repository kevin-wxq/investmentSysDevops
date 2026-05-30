package com.ruoyi.devops.service;

import java.util.List;
import com.ruoyi.devops.domain.OpsItemRelation;

/**
 * 事项关系Service接口
 *
 * @author ruoyi
 * @date 2026-05-30
 */
public interface IOpsItemRelationService
{
    public OpsItemRelation selectOpsItemRelationById(Long id);

    public List<OpsItemRelation> selectOpsItemRelationList(OpsItemRelation opsItemRelation);

    public int insertOpsItemRelation(OpsItemRelation opsItemRelation);

    public int updateOpsItemRelation(OpsItemRelation opsItemRelation);

    public int deleteOpsItemRelationByIds(Long[] ids);

    public int deleteOpsItemRelationById(Long id);
}
