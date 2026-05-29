package com.ruoyi.devops.service;

import java.util.List;
import com.ruoyi.devops.domain.OpsDutyShift;

/**
 * 值班班次Service接口
 * 
 * @author ruoyi
 * @date 2026-05-30
 */
public interface IOpsDutyShiftService 
{
    /**
     * 查询值班班次
     * 
     * @param id 值班班次主键
     * @return 值班班次
     */
    public OpsDutyShift selectOpsDutyShiftById(Long id);

    /**
     * 查询值班班次列表
     * 
     * @param opsDutyShift 值班班次
     * @return 值班班次集合
     */
    public List<OpsDutyShift> selectOpsDutyShiftList(OpsDutyShift opsDutyShift);

    /**
     * 新增值班班次
     * 
     * @param opsDutyShift 值班班次
     * @return 结果
     */
    public int insertOpsDutyShift(OpsDutyShift opsDutyShift);

    /**
     * 修改值班班次
     * 
     * @param opsDutyShift 值班班次
     * @return 结果
     */
    public int updateOpsDutyShift(OpsDutyShift opsDutyShift);

    /**
     * 批量删除值班班次
     * 
     * @param ids 需要删除的值班班次主键集合
     * @return 结果
     */
    public int deleteOpsDutyShiftByIds(Long[] ids);

    /**
     * 删除值班班次信息
     * 
     * @param id 值班班次主键
     * @return 结果
     */
    public int deleteOpsDutyShiftById(Long id);
}
