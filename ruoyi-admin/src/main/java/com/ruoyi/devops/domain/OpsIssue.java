package com.ruoyi.devops.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 运维问题对象 ops_issue
 *
 * @author ruoyi
 * @date 2026-05-30
 */
public class OpsIssue extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long id;

    @Excel(name = "问题编号")
    private String issueNo;

    @Excel(name = "问题类型")
    private String issueType;

    private Long systemId;

    @Excel(name = "系统名称")
    private String systemName;

    @Excel(name = "所属模块")
    private String moduleCode;

    @Excel(name = "问题标题")
    private String issueTitle;

    @Excel(name = "问题描述")
    private String issueDesc;

    @Excel(name = "发现来源")
    private String sourceType;

    @Excel(name = "影响范围")
    private String impactScope;

    @Excel(name = "优先级")
    private String priority;

    @Excel(name = "发现人")
    private String founder;

    @Excel(name = "发现时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date foundTime;

    private Long ownerId;

    @Excel(name = "负责人")
    private String ownerName;

    @Excel(name = "当前状态")
    private String status;

    @Excel(name = "处理方式")
    private String handleMethod;

    @Excel(name = "处理结果")
    private String handleResult;

    @Excel(name = "根因分类")
    private String rootCause;

    private Long workItemId;

    @Excel(name = "关联Bug")
    private String relatedBugNo;

    @Excel(name = "关联需求")
    private String relatedReqNo;

    @Excel(name = "关联变更")
    private String relatedChangeNo;

    private String delFlag;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getIssueNo() { return issueNo; }
    public void setIssueNo(String issueNo) { this.issueNo = issueNo; }
    public String getIssueType() { return issueType; }
    public void setIssueType(String issueType) { this.issueType = issueType; }
    public Long getSystemId() { return systemId; }
    public void setSystemId(Long systemId) { this.systemId = systemId; }
    public String getSystemName() { return systemName; }
    public void setSystemName(String systemName) { this.systemName = systemName; }
    public String getModuleCode() { return moduleCode; }
    public void setModuleCode(String moduleCode) { this.moduleCode = moduleCode; }
    public String getIssueTitle() { return issueTitle; }
    public void setIssueTitle(String issueTitle) { this.issueTitle = issueTitle; }
    public String getIssueDesc() { return issueDesc; }
    public void setIssueDesc(String issueDesc) { this.issueDesc = issueDesc; }
    public String getSourceType() { return sourceType; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }
    public String getImpactScope() { return impactScope; }
    public void setImpactScope(String impactScope) { this.impactScope = impactScope; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getFounder() { return founder; }
    public void setFounder(String founder) { this.founder = founder; }
    public Date getFoundTime() { return foundTime; }
    public void setFoundTime(Date foundTime) { this.foundTime = foundTime; }
    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getHandleMethod() { return handleMethod; }
    public void setHandleMethod(String handleMethod) { this.handleMethod = handleMethod; }
    public String getHandleResult() { return handleResult; }
    public void setHandleResult(String handleResult) { this.handleResult = handleResult; }
    public String getRootCause() { return rootCause; }
    public void setRootCause(String rootCause) { this.rootCause = rootCause; }
    public Long getWorkItemId() { return workItemId; }
    public void setWorkItemId(Long workItemId) { this.workItemId = workItemId; }
    public String getRelatedBugNo() { return relatedBugNo; }
    public void setRelatedBugNo(String relatedBugNo) { this.relatedBugNo = relatedBugNo; }
    public String getRelatedReqNo() { return relatedReqNo; }
    public void setRelatedReqNo(String relatedReqNo) { this.relatedReqNo = relatedReqNo; }
    public String getRelatedChangeNo() { return relatedChangeNo; }
    public void setRelatedChangeNo(String relatedChangeNo) { this.relatedChangeNo = relatedChangeNo; }
    public String getDelFlag() { return delFlag; }
    public void setDelFlag(String delFlag) { this.delFlag = delFlag; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("issueNo", getIssueNo())
            .append("issueType", getIssueType())
            .append("systemName", getSystemName())
            .append("moduleCode", getModuleCode())
            .append("issueTitle", getIssueTitle())
            .append("priority", getPriority())
            .append("status", getStatus())
            .append("handleMethod", getHandleMethod())
            .append("workItemId", getWorkItemId())
            .toString();
    }
}
