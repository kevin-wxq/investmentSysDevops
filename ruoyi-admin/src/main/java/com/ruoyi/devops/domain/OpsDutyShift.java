package com.ruoyi.devops.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 值班班次对象 ops_duty_shift
 * 
 * @author ruoyi
 * @date 2026-05-30
 */
public class OpsDutyShift extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 班次名称 */
    @Excel(name = "班次名称")
    private String shiftName;

    /** 班次日期 */
    @Excel(name = "班次日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date shiftDate;

    /** 班次类型(1白班 2夜班 3节假日) */
    @Excel(name = "班次类型")
    private String shiftType;

    /** 带班领导ID */
    private Long leaderId;

    /** 带班领导姓名 */
    @Excel(name = "带班领导")
    private String leaderName;

    /** 值班成员ID(逗号分隔) */
    private String memberIds;

    /** 交接备注 */
    @Excel(name = "交接备注")
    private String handoverNotes;

    /** 班次状态(1进行中 2已交接 3已结束) */
    @Excel(name = "班次状态")
    private String shiftStatus;

    /** 删除标志(0存在 2删除) */
    private String delFlag;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setShiftName(String shiftName) 
    {
        this.shiftName = shiftName;
    }

    public String getShiftName() 
    {
        return shiftName;
    }

    public void setShiftDate(Date shiftDate) 
    {
        this.shiftDate = shiftDate;
    }

    public Date getShiftDate() 
    {
        return shiftDate;
    }

    public void setShiftType(String shiftType) 
    {
        this.shiftType = shiftType;
    }

    public String getShiftType() 
    {
        return shiftType;
    }

    public void setLeaderId(Long leaderId) 
    {
        this.leaderId = leaderId;
    }

    public Long getLeaderId() 
    {
        return leaderId;
    }

    public void setLeaderName(String leaderName) 
    {
        this.leaderName = leaderName;
    }

    public String getLeaderName() 
    {
        return leaderName;
    }

    public void setMemberIds(String memberIds) 
    {
        this.memberIds = memberIds;
    }

    public String getMemberIds() 
    {
        return memberIds;
    }

    public void setHandoverNotes(String handoverNotes) 
    {
        this.handoverNotes = handoverNotes;
    }

    public String getHandoverNotes() 
    {
        return handoverNotes;
    }

    public void setShiftStatus(String shiftStatus) 
    {
        this.shiftStatus = shiftStatus;
    }

    public String getShiftStatus() 
    {
        return shiftStatus;
    }

    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("shiftName", getShiftName())
            .append("shiftDate", getShiftDate())
            .append("shiftType", getShiftType())
            .append("leaderId", getLeaderId())
            .append("leaderName", getLeaderName())
            .append("memberIds", getMemberIds())
            .append("handoverNotes", getHandoverNotes())
            .append("shiftStatus", getShiftStatus())
            .append("delFlag", getDelFlag())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
