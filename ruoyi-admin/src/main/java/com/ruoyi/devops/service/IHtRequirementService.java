package com.ruoyi.devops.service;

import java.util.Date;
import java.util.List;
import com.ruoyi.devops.domain.HtRequirement;

/**
 * 衡泰需求Service接口
 *
 * @author ruoyi
 * @date 2026-05-30
 */
public interface IHtRequirementService
{
    public HtRequirement selectHtRequirementById(Long id);

    public List<HtRequirement> selectHtRequirementList(HtRequirement htRequirement);

    public String generateReqNo(String deptCode, Date submitDate);

    public int insertHtRequirement(HtRequirement htRequirement);

    public int updateHtRequirement(HtRequirement htRequirement);

    public int deleteHtRequirementByIds(Long[] ids);

    public int deleteHtRequirementById(Long id);
}
