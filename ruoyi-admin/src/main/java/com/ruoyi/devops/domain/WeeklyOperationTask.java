package com.ruoyi.devops.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class WeeklyOperationTask extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;

    /** 周报ID */
    private Long reportId;

    @Excel(name = "任务名称")
    private String taskName;

    @Excel(name = "执行人")
    private String executor;

    @Excel(name = "需求方")
    private String requester;

    @Excel(name = "计划日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date plannedDate;

    @Excel(name = "任务状态")
    private String taskStatus;

    @Excel(name = "需求描述")
    private String requirementDesc;

    @Excel(name = "本周推进")
    private String thisWeekProgress;

    @Excel(name = "下周计划")
    private String nextWeekPlan;

    @Excel(name = "问题风险")
    private String issueRisk;

    /** 删除标志(0存在 2删除) */
    private String delFlag;

    public void setId(Long id) { this.id = id; }
    public Long getId() { return id; }

    public void setReportId(Long reportId) { this.reportId = reportId; }
    public Long getReportId() { return reportId; }

    public void setTaskName(String taskName) { this.taskName = taskName; }
    public String getTaskName() { return taskName; }

    public void setExecutor(String executor) { this.executor = executor; }
    public String getExecutor() { return executor; }

    public void setRequester(String requester) { this.requester = requester; }
    public String getRequester() { return requester; }

    public void setPlannedDate(Date plannedDate) { this.plannedDate = plannedDate; }
    public Date getPlannedDate() { return plannedDate; }

    public void setTaskStatus(String taskStatus) { this.taskStatus = taskStatus; }
    public String getTaskStatus() { return taskStatus; }

    public void setRequirementDesc(String requirementDesc) { this.requirementDesc = requirementDesc; }
    public String getRequirementDesc() { return requirementDesc; }

    public void setThisWeekProgress(String thisWeekProgress) { this.thisWeekProgress = thisWeekProgress; }
    public String getThisWeekProgress() { return thisWeekProgress; }

    public void setNextWeekPlan(String nextWeekPlan) { this.nextWeekPlan = nextWeekPlan; }
    public String getNextWeekPlan() { return nextWeekPlan; }

    public void setIssueRisk(String issueRisk) { this.issueRisk = issueRisk; }
    public String getIssueRisk() { return issueRisk; }

    public void setDelFlag(String delFlag) { this.delFlag = delFlag; }
    public String getDelFlag() { return delFlag; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("reportId", getReportId())
            .append("taskName", getTaskName())
            .append("executor", getExecutor())
            .append("requester", getRequester())
            .append("plannedDate", getPlannedDate())
            .append("taskStatus", getTaskStatus())
            .append("requirementDesc", getRequirementDesc())
            .append("thisWeekProgress", getThisWeekProgress())
            .append("nextWeekPlan", getNextWeekPlan())
            .append("issueRisk", getIssueRisk())
            .append("delFlag", getDelFlag())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
