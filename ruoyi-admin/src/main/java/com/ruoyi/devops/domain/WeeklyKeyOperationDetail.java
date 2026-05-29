package com.ruoyi.devops.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class WeeklyKeyOperationDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;

    /** 任务ID */
    private Long taskId;

    @Excel(name = "运维类型")
    private String operationType;

    @Excel(name = "运维名称")
    private String operationName;

    @Excel(name = "系统名称")
    private String systemName;

    @Excel(name = "运维内容")
    private String operationContent;

    @Excel(name = "执行人")
    private String executor;

    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    @Excel(name = "执行结果")
    private String operationResult;

    @Excel(name = "影响系统")
    private String affectedSystems;

    @Excel(name = "验证方法")
    private String verifyMethod;

    @Excel(name = "验证结果")
    private String verifyResult;

    @Excel(name = "发现问题")
    private String issuesFound;

    /** 删除标志(0存在 2删除) */
    private String delFlag;

    public void setId(Long id) { this.id = id; }
    public Long getId() { return id; }

    public void setTaskId(Long taskId) { this.taskId = taskId; }
    public Long getTaskId() { return taskId; }

    public void setOperationType(String operationType) { this.operationType = operationType; }
    public String getOperationType() { return operationType; }

    public void setOperationName(String operationName) { this.operationName = operationName; }
    public String getOperationName() { return operationName; }

    public void setSystemName(String systemName) { this.systemName = systemName; }
    public String getSystemName() { return systemName; }

    public void setOperationContent(String operationContent) { this.operationContent = operationContent; }
    public String getOperationContent() { return operationContent; }

    public void setExecutor(String executor) { this.executor = executor; }
    public String getExecutor() { return executor; }

    public void setStartTime(Date startTime) { this.startTime = startTime; }
    public Date getStartTime() { return startTime; }

    public void setEndTime(Date endTime) { this.endTime = endTime; }
    public Date getEndTime() { return endTime; }

    public void setOperationResult(String operationResult) { this.operationResult = operationResult; }
    public String getOperationResult() { return operationResult; }

    public void setAffectedSystems(String affectedSystems) { this.affectedSystems = affectedSystems; }
    public String getAffectedSystems() { return affectedSystems; }

    public void setVerifyMethod(String verifyMethod) { this.verifyMethod = verifyMethod; }
    public String getVerifyMethod() { return verifyMethod; }

    public void setVerifyResult(String verifyResult) { this.verifyResult = verifyResult; }
    public String getVerifyResult() { return verifyResult; }

    public void setIssuesFound(String issuesFound) { this.issuesFound = issuesFound; }
    public String getIssuesFound() { return issuesFound; }

    public void setDelFlag(String delFlag) { this.delFlag = delFlag; }
    public String getDelFlag() { return delFlag; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("taskId", getTaskId())
            .append("operationType", getOperationType())
            .append("operationName", getOperationName())
            .append("systemName", getSystemName())
            .append("operationContent", getOperationContent())
            .append("executor", getExecutor())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("operationResult", getOperationResult())
            .append("affectedSystems", getAffectedSystems())
            .append("verifyMethod", getVerifyMethod())
            .append("verifyResult", getVerifyResult())
            .append("issuesFound", getIssuesFound())
            .append("delFlag", getDelFlag())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
