package com.ruoyi.devops.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.devops.domain.HtRequirement;

/**
 * 衡泰需求Mapper接口
 *
 * @author ruoyi
 * @date 2026-05-30
 */
public interface HtRequirementMapper
{
    public HtRequirement selectHtRequirementById(Long id);

    public List<HtRequirement> selectHtRequirementList(HtRequirement htRequirement);

    public String selectMaxReqNoByPrefix(@Param("prefix") String prefix);

    public int insertHtRequirement(HtRequirement htRequirement);

    public int updateHtRequirement(HtRequirement htRequirement);

    public int deleteHtRequirementById(Long id);

    public int deleteHtRequirementByIds(Long[] ids);
}
