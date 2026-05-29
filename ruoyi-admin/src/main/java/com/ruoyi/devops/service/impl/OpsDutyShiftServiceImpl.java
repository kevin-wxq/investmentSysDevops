package com.ruoyi.devops.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.devops.mapper.OpsDutyShiftMapper;
import com.ruoyi.devops.domain.OpsDutyShift;
import com.ruoyi.devops.service.IOpsDutyShiftService;

/**
 * 值班班次Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-05-30
 */
@Service
public class OpsDutyShiftServiceImpl implements IOpsDutyShiftService 
{
    @Autowired
    private OpsDutyShiftMapper opsDutyShiftMapper;

    /**
     * 查询值班班次
     * 
     * @param id 值班班次主键
     * @return 值班班次
     */
    @Override
    public OpsDutyShift selectOpsDutyShiftById(Long id)
    {
        return opsDutyShiftMapper.selectOpsDutyShiftById(id);
    }

    /**
     * 查询值班班次列表
     * 
     * @param opsDutyShift 值班班次
     * @return 值班班次
     */
    @Override
    public List<OpsDutyShift> selectOpsDutyShiftList(OpsDutyShift opsDutyShift)
    {
        return opsDutyShiftMapper.selectOpsDutyShiftList(opsDutyShift);
    }

    /**
     * 新增值班班次
     * 
     * @param opsDutyShift 值班班次
     * @return 结果
     */
    @Override
    public int insertOpsDutyShift(OpsDutyShift opsDutyShift)
    {
        opsDutyShift.setCreateTime(DateUtils.getNowDate());
        return opsDutyShiftMapper.insertOpsDutyShift(opsDutyShift);
    }

    /**
     * 修改值班班次
     * 
     * @param opsDutyShift 值班班次
     * @return 结果
     */
    @Override
    public int updateOpsDutyShift(OpsDutyShift opsDutyShift)
    {
        opsDutyShift.setUpdateTime(DateUtils.getNowDate());
        return opsDutyShiftMapper.updateOpsDutyShift(opsDutyShift);
    }

    /**
     * 批量删除值班班次
     * 
     * @param ids 需要删除的值班班次主键
     * @return 结果
     */
    @Override
    public int deleteOpsDutyShiftByIds(Long[] ids)
    {
        return opsDutyShiftMapper.deleteOpsDutyShiftByIds(ids);
    }

    /**
     * 删除值班班次信息
     * 
     * @param id 值班班次主键
     * @return 结果
     */
    @Override
    public int deleteOpsDutyShiftById(Long id)
    {
        return opsDutyShiftMapper.deleteOpsDutyShiftById(id);
    }
}
