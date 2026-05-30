package com.ruoyi.devops.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.devops.domain.OpsItemRelation;
import com.ruoyi.devops.mapper.OpsItemRelationMapper;
import com.ruoyi.devops.service.IOpsItemRelationService;

@Service
public class OpsItemRelationServiceImpl implements IOpsItemRelationService {
    @Autowired
    private OpsItemRelationMapper opsItemRelationMapper;

    @Override
    public OpsItemRelation selectOpsItemRelationById(Long id) {
        return opsItemRelationMapper.selectOpsItemRelationById(id);
    }

    @Override
    public List<OpsItemRelation> selectOpsItemRelationList(OpsItemRelation opsItemRelation) {
        return opsItemRelationMapper.selectOpsItemRelationList(opsItemRelation);
    }

    @Override
    public int insertOpsItemRelation(OpsItemRelation opsItemRelation) {
        if (opsItemRelation.getRelationTime() == null) {
            opsItemRelation.setRelationTime(DateUtils.getNowDate());
        }
        if (StringUtils.isEmpty(opsItemRelation.getRelationType())) {
            opsItemRelation.setRelationType("RELATE_TO");
        }
        if (StringUtils.isEmpty(opsItemRelation.getDelFlag())) {
            opsItemRelation.setDelFlag("0");
        }
        opsItemRelation.setCreateTime(DateUtils.getNowDate());
        return opsItemRelationMapper.insertOpsItemRelation(opsItemRelation);
    }

    @Override
    public int updateOpsItemRelation(OpsItemRelation opsItemRelation) {
        opsItemRelation.setUpdateTime(DateUtils.getNowDate());
        return opsItemRelationMapper.updateOpsItemRelation(opsItemRelation);
    }

    @Override
    public int deleteOpsItemRelationByIds(Long[] ids) {
        return opsItemRelationMapper.deleteOpsItemRelationByIds(ids);
    }

    @Override
    public int deleteOpsItemRelationById(Long id) {
        return opsItemRelationMapper.deleteOpsItemRelationById(id);
    }
}
