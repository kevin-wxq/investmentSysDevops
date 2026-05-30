package com.ruoyi.devops.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * Bug记录对象 ht_bug_record
 *
 * @author ruoyi
 * @date 2026-05-30
 */
public class HtBugRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long id;

    @Excel(name = "Bug编号")
    private String bugNo;

    private Long requirementId;

    @Excel(name = "关联需求编号")
    private String requirementNo;

    private Long systemId;

    @Excel(name = "系统名称")
    private String systemName;

    @Excel(name = "Bug标题")
    private String bugTitle;

    @Excel(name = "Bug描述")
    private String bugDesc;

    @Excel(name = "严重程度")
    private String severity;

    @Excel(name = "优先级")
    private String priority;

    @Excel(name = "状态")
    private String status;

    @Excel(name = "发现人")
    private String founder;

    @Excel(name = "发现时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date foundTime;

    private Long ownerId;

    @Excel(name = "负责人")
    private String ownerName;

    @Excel(name = "修复计划")
    private String fixPlan;

    @Excel(name = "修复结果")
    private String fixResult;

    @Excel(name = "计划修复时间", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date planFixTime;

    @Excel(name = "实际修复时间", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date actualFixTime;

    @Excel(name = "测试人")
    private String tester;

    @Excel(name = "验收结果")
    private String testResult;


    @Excel(name = "验证人")
    private String verifierName;

    private Long verifierId;

    @Excel(name = "验证时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date verifyTime;

    @Excel(name = "验证结果")
    private String verifyResult;

    @Excel(name = "验证说明")
    private String verifyDetail;

    @Excel(name = "补丁号")
    private String patchNo;

    @Excel(name = "修复完成时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fixCompleteTime;

    @Excel(name = "上线时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date onlineTime;

    @Excel(name = "关闭时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date closeTime;

    @Excel(name = "关闭说明")
    private String closeDesc;

    private Long workItemId;

    private String delFlag;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBugNo() { return bugNo; }
    public void setBugNo(String bugNo) { this.bugNo = bugNo; }
    public Long getRequirementId() { return requirementId; }
    public void setRequirementId(Long requirementId) { this.requirementId = requirementId; }
    public String getRequirementNo() { return requirementNo; }
    public void setRequirementNo(String requirementNo) { this.requirementNo = requirementNo; }
    public Long getSystemId() { return systemId; }
    public void setSystemId(Long systemId) { this.systemId = systemId; }
    public String getSystemName() { return systemName; }
    public void setSystemName(String systemName) { this.systemName = systemName; }
    public String getBugTitle() { return bugTitle; }
    public void setBugTitle(String bugTitle) { this.bugTitle = bugTitle; }
    public String getBugDesc() { return bugDesc; }
    public void setBugDesc(String bugDesc) { this.bugDesc = bugDesc; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getFounder() { return founder; }
    public void setFounder(String founder) { this.founder = founder; }
    public Date getFoundTime() { return foundTime; }
    public void setFoundTime(Date foundTime) { this.foundTime = foundTime; }
    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
    public String getFixPlan() { return fixPlan; }
    public void setFixPlan(String fixPlan) { this.fixPlan = fixPlan; }
    public String getFixResult() { return fixResult; }
    public void setFixResult(String fixResult) { this.fixResult = fixResult; }
    public Date getPlanFixTime() { return planFixTime; }
    public void setPlanFixTime(Date planFixTime) { this.planFixTime = planFixTime; }
    public Date getActualFixTime() { return actualFixTime; }
    public void setActualFixTime(Date actualFixTime) { this.actualFixTime = actualFixTime; }
    public String getTester() { return tester; }
    public void setTester(String tester) { this.tester = tester; }
    public String getTestResult() { return testResult; }
    public void setTestResult(String testResult) { this.testResult = testResult; }
    public Long getVerifierId() { return verifierId; }
    public void setVerifierId(Long verifierId) { this.verifierId = verifierId; }
    public String getVerifierName() { return verifierName; }
    public void setVerifierName(String verifierName) { this.verifierName = verifierName; }
    public Date getVerifyTime() { return verifyTime; }
    public void setVerifyTime(Date verifyTime) { this.verifyTime = verifyTime; }
    public String getVerifyResult() { return verifyResult; }
    public void setVerifyResult(String verifyResult) { this.verifyResult = verifyResult; }
    public String getVerifyDetail() { return verifyDetail; }
    public void setVerifyDetail(String verifyDetail) { this.verifyDetail = verifyDetail; }
    public String getPatchNo() { return patchNo; }
    public void setPatchNo(String patchNo) { this.patchNo = patchNo; }
    public Date getFixCompleteTime() { return fixCompleteTime; }
    public void setFixCompleteTime(Date fixCompleteTime) { this.fixCompleteTime = fixCompleteTime; }
    public Date getOnlineTime() { return onlineTime; }
    public void setOnlineTime(Date onlineTime) { this.onlineTime = onlineTime; }
    public Date getCloseTime() { return closeTime; }
    public void setCloseTime(Date closeTime) { this.closeTime = closeTime; }
    public String getCloseDesc() { return closeDesc; }
    public void setCloseDesc(String closeDesc) { this.closeDesc = closeDesc; }
    public Long getWorkItemId() { return workItemId; }
    public void setWorkItemId(Long workItemId) { this.workItemId = workItemId; }
    public String getDelFlag() { return delFlag; }
    public void setDelFlag(String delFlag) { this.delFlag = delFlag; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("bugNo", getBugNo())
            .append("requirementNo", getRequirementNo())
            .append("systemName", getSystemName())
            .append("bugTitle", getBugTitle())
            .append("severity", getSeverity())
            .append("priority", getPriority())
            .append("status", getStatus())
            .append("workItemId", getWorkItemId())
            .toString();
    }
}
