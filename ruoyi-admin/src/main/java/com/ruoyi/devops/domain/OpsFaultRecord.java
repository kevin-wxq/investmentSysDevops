package com.ruoyi.devops.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 故障记录对象 ops_fault_record
 * 
 * @author ruoyi
 * @date 2026-05-30
 */
public class OpsFaultRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 系统ID */
    private Long systemId;

    /** 系统名称 */
    @Excel(name = "系统名称")
    private String systemName;

    /** 故障标题 */
    @Excel(name = "故障标题")
    private String faultTitle;

    /** 故障描述 */
    @Excel(name = "故障描述")
    private String faultDesc;

    /** 故障级别(1一般 2严重 3紧急) */
    @Excel(name = "故障级别")
    private String faultLevel;

    /** 故障状态(1发现 2处理中 3已恢复 4已关闭) */
    @Excel(name = "故障状态")
    private String faultStatus;

    /** 发生时间 */
    @Excel(name = "发生时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date occurTime;

    /** 发现人ID */
    private Long discovererId;

    /** 发现人姓名 */
    @Excel(name = "发现人")
    private String discovererName;

    /** 处理人ID */
    private Long handlerId;

    /** 处理人姓名 */
    @Excel(name = "处理人")
    private String handlerName;

    /** 恢复时间 */
    @Excel(name = "恢复时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date recoverTime;

    /** 持续时长(分钟) */
    @Excel(name = "持续时长(分钟)")
    private Integer durationMinutes;

    /** 根因分析 */
    @Excel(name = "根因分析")
    private String rootCause;

    /** 解决方案 */
    @Excel(name = "解决方案")
    private String solution;

    /** 预防措施 */
    @Excel(name = "预防措施")
    private String preventiveMeasures;

    /** 影响范围 */
    @Excel(name = "影响范围")
    private String affectedScope;

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

    public void setSystemId(Long systemId) 
    {
        this.systemId = systemId;
    }

    public Long getSystemId() 
    {
        return systemId;
    }

    public void setSystemName(String systemName) 
    {
        this.systemName = systemName;
    }

    public String getSystemName() 
    {
        return systemName;
    }

    public void setFaultTitle(String faultTitle) 
    {
        this.faultTitle = faultTitle;
    }

    public String getFaultTitle() 
    {
        return faultTitle;
    }

    public void setFaultDesc(String faultDesc) 
    {
        this.faultDesc = faultDesc;
    }

    public String getFaultDesc() 
    {
        return faultDesc;
    }

    public void setFaultLevel(String faultLevel) 
    {
        this.faultLevel = faultLevel;
    }

    public String getFaultLevel() 
    {
        return faultLevel;
    }

    public void setFaultStatus(String faultStatus) 
    {
        this.faultStatus = faultStatus;
    }

    public String getFaultStatus() 
    {
        return faultStatus;
    }

    public void setOccurTime(Date occurTime) 
    {
        this.occurTime = occurTime;
    }

    public Date getOccurTime() 
    {
        return occurTime;
    }

    public void setDiscovererId(Long discovererId) 
    {
        this.discovererId = discovererId;
    }

    public Long getDiscovererId() 
    {
        return discovererId;
    }

    public void setDiscovererName(String discovererName) 
    {
        this.discovererName = discovererName;
    }

    public String getDiscovererName() 
    {
        return discovererName;
    }

    public void setHandlerId(Long handlerId) 
    {
        this.handlerId = handlerId;
    }

    public Long getHandlerId() 
    {
        return handlerId;
    }

    public void setHandlerName(String handlerName) 
    {
        this.handlerName = handlerName;
    }

    public String getHandlerName() 
    {
        return handlerName;
    }

    public void setRecoverTime(Date recoverTime) 
    {
        this.recoverTime = recoverTime;
    }

    public Date getRecoverTime() 
    {
        return recoverTime;
    }

    public void setDurationMinutes(Integer durationMinutes) 
    {
        this.durationMinutes = durationMinutes;
    }

    public Integer getDurationMinutes() 
    {
        return durationMinutes;
    }

    public void setRootCause(String rootCause) 
    {
        this.rootCause = rootCause;
    }

    public String getRootCause() 
    {
        return rootCause;
    }

    public void setSolution(String solution) 
    {
        this.solution = solution;
    }

    public String getSolution() 
    {
        return solution;
    }

    public void setPreventiveMeasures(String preventiveMeasures) 
    {
        this.preventiveMeasures = preventiveMeasures;
    }

    public String getPreventiveMeasures() 
    {
        return preventiveMeasures;
    }

    public void setAffectedScope(String affectedScope) 
    {
        this.affectedScope = affectedScope;
    }

    public String getAffectedScope() 
    {
        return affectedScope;
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
            .append("systemId", getSystemId())
            .append("systemName", getSystemName())
            .append("faultTitle", getFaultTitle())
            .append("faultDesc", getFaultDesc())
            .append("faultLevel", getFaultLevel())
            .append("faultStatus", getFaultStatus())
            .append("occurTime", getOccurTime())
            .append("discovererId", getDiscovererId())
            .append("discovererName", getDiscovererName())
            .append("handlerId", getHandlerId())
            .append("handlerName", getHandlerName())
            .append("recoverTime", getRecoverTime())
            .append("durationMinutes", getDurationMinutes())
            .append("rootCause", getRootCause())
            .append("solution", getSolution())
            .append("preventiveMeasures", getPreventiveMeasures())
            .append("affectedScope", getAffectedScope())
            .append("delFlag", getDelFlag())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
