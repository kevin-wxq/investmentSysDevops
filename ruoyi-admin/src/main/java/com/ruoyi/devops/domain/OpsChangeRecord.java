package com.ruoyi.devops.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 变更记录对象 ops_change_record
 * 
 * @author ruoyi
 * @date 2026-05-30
 */
public class OpsChangeRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 系统ID */
    @Excel(name = "系统ID")
    private Long systemId;

    /** 变更类型 */
    @Excel(name = "变更类型")
    private String changeType;

    /** 变更标题 */
    @Excel(name = "变更标题")
    private String changeTitle;

    /** 变更描述 */
    private String changeDesc;

    /** 风险等级 */
    @Excel(name = "风险等级")
    private String riskLevel;

    /** 变更内容 */
    private String changeContent;

    /** 回滚方案 */
    private String rollbackPlan;

    /** 变更时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "变更时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date changeTime;

    /** 执行人ID */
    @Excel(name = "执行人ID")
    private Long executorId;

    /** 变更结果 */
    @Excel(name = "变更结果")
    private String changeResult;

    /** 审批人ID */
    @Excel(name = "审批人ID")
    private Long approverId;

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

    public void setChangeType(String changeType) 
    {
        this.changeType = changeType;
    }

    public String getChangeType() 
    {
        return changeType;
    }

    public void setChangeTitle(String changeTitle) 
    {
        this.changeTitle = changeTitle;
    }

    public String getChangeTitle() 
    {
        return changeTitle;
    }

    public void setChangeDesc(String changeDesc) 
    {
        this.changeDesc = changeDesc;
    }

    public String getChangeDesc() 
    {
        return changeDesc;
    }

    public void setRiskLevel(String riskLevel) 
    {
        this.riskLevel = riskLevel;
    }

    public String getRiskLevel() 
    {
        return riskLevel;
    }

    public void setChangeContent(String changeContent) 
    {
        this.changeContent = changeContent;
    }

    public String getChangeContent() 
    {
        return changeContent;
    }

    public void setRollbackPlan(String rollbackPlan) 
    {
        this.rollbackPlan = rollbackPlan;
    }

    public String getRollbackPlan() 
    {
        return rollbackPlan;
    }

    public void setChangeTime(Date changeTime) 
    {
        this.changeTime = changeTime;
    }

    public Date getChangeTime() 
    {
        return changeTime;
    }

    public void setExecutorId(Long executorId) 
    {
        this.executorId = executorId;
    }

    public Long getExecutorId() 
    {
        return executorId;
    }

    public void setChangeResult(String changeResult) 
    {
        this.changeResult = changeResult;
    }

    public String getChangeResult() 
    {
        return changeResult;
    }

    public void setApproverId(Long approverId) 
    {
        this.approverId = approverId;
    }

    public Long getApproverId() 
    {
        return approverId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("systemId", getSystemId())
            .append("changeType", getChangeType())
            .append("changeTitle", getChangeTitle())
            .append("changeDesc", getChangeDesc())
            .append("riskLevel", getRiskLevel())
            .append("changeContent", getChangeContent())
            .append("rollbackPlan", getRollbackPlan())
            .append("changeTime", getChangeTime())
            .append("executorId", getExecutorId())
            .append("changeResult", getChangeResult())
            .append("approverId", getApproverId())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
