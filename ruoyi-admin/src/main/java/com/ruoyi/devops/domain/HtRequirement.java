package com.ruoyi.devops.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 衡泰需求对象 ht_requirement
 *
 * @author ruoyi
 * @date 2026-05-30
 */
public class HtRequirement extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long id;

    @Excel(name = "需求编号")
    private String reqNo;

    @Excel(name = "提出部门")
    private String deptCode;

    @Excel(name = "需求名称")
    private String reqName;

    @Excel(name = "优先级")
    private String priority;

    @Excel(name = "所属模块")
    private String moduleCode;

    private Long systemId;

    @Excel(name = "系统名称")
    private String systemName;

    @Excel(name = "需求描述")
    private String reqDesc;

    @Excel(name = "业务价值")
    private String businessValue;

    @Excel(name = "是否涉及商务")
    private String businessFlag;

    @Excel(name = "提出人")
    private String submitter;

    @Excel(name = "提出时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date submitTime;

    @Excel(name = "预计上线时间", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expectedOnlineTime;

    @Excel(name = "厂商分析人")
    private String vendorAnalyst;

    @Excel(name = "分析结果")
    private String analysisResult;

    @Excel(name = "计划排期时间", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date planScheduleTime;

    @Excel(name = "开发完成时间", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date devFinishTime;

    @Excel(name = "验收人")
    private String acceptor;

    @Excel(name = "验收结果")
    private String acceptanceResult;

    @Excel(name = "上线时间", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date onlineTime;


    @Excel(name = "补丁号")
    private String patchNo;

    @Excel(name = "分析详情")
    private String analysisDetail;

    @Excel(name = "验收详情")
    private String acceptanceDetail;

    @Excel(name = "当前状态")
    private String status;

    @Excel(name = "处理进展")
    private String progress;

    private Long workItemId;

    private String delFlag;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getReqNo() { return reqNo; }
    public void setReqNo(String reqNo) { this.reqNo = reqNo; }
    public String getDeptCode() { return deptCode; }
    public void setDeptCode(String deptCode) { this.deptCode = deptCode; }
    public String getReqName() { return reqName; }
    public void setReqName(String reqName) { this.reqName = reqName; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getModuleCode() { return moduleCode; }
    public void setModuleCode(String moduleCode) { this.moduleCode = moduleCode; }
    public Long getSystemId() { return systemId; }
    public void setSystemId(Long systemId) { this.systemId = systemId; }
    public String getSystemName() { return systemName; }
    public void setSystemName(String systemName) { this.systemName = systemName; }
    public String getReqDesc() { return reqDesc; }
    public void setReqDesc(String reqDesc) { this.reqDesc = reqDesc; }
    public String getBusinessValue() { return businessValue; }
    public void setBusinessValue(String businessValue) { this.businessValue = businessValue; }
    public String getBusinessFlag() { return businessFlag; }
    public void setBusinessFlag(String businessFlag) { this.businessFlag = businessFlag; }
    public String getSubmitter() { return submitter; }
    public void setSubmitter(String submitter) { this.submitter = submitter; }
    public Date getSubmitTime() { return submitTime; }
    public void setSubmitTime(Date submitTime) { this.submitTime = submitTime; }
    public Date getExpectedOnlineTime() { return expectedOnlineTime; }
    public void setExpectedOnlineTime(Date expectedOnlineTime) { this.expectedOnlineTime = expectedOnlineTime; }
    public String getVendorAnalyst() { return vendorAnalyst; }
    public void setVendorAnalyst(String vendorAnalyst) { this.vendorAnalyst = vendorAnalyst; }
    public String getAnalysisResult() { return analysisResult; }
    public void setAnalysisResult(String analysisResult) { this.analysisResult = analysisResult; }
    public Date getPlanScheduleTime() { return planScheduleTime; }
    public void setPlanScheduleTime(Date planScheduleTime) { this.planScheduleTime = planScheduleTime; }
    public Date getDevFinishTime() { return devFinishTime; }
    public void setDevFinishTime(Date devFinishTime) { this.devFinishTime = devFinishTime; }
    public String getAcceptor() { return acceptor; }
    public void setAcceptor(String acceptor) { this.acceptor = acceptor; }
    public String getAcceptanceResult() { return acceptanceResult; }
    public void setAcceptanceResult(String acceptanceResult) { this.acceptanceResult = acceptanceResult; }
    public Date getOnlineTime() { return onlineTime; }
    public void setOnlineTime(Date onlineTime) { this.onlineTime = onlineTime; }
    public String getPatchNo() { return patchNo; }
    public void setPatchNo(String patchNo) { this.patchNo = patchNo; }
    public String getAnalysisDetail() { return analysisDetail; }
    public void setAnalysisDetail(String analysisDetail) { this.analysisDetail = analysisDetail; }
    public String getAcceptanceDetail() { return acceptanceDetail; }
    public void setAcceptanceDetail(String acceptanceDetail) { this.acceptanceDetail = acceptanceDetail; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getProgress() { return progress; }
    public void setProgress(String progress) { this.progress = progress; }
    public Long getWorkItemId() { return workItemId; }
    public void setWorkItemId(Long workItemId) { this.workItemId = workItemId; }
    public String getDelFlag() { return delFlag; }
    public void setDelFlag(String delFlag) { this.delFlag = delFlag; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("reqNo", getReqNo())
            .append("deptCode", getDeptCode())
            .append("reqName", getReqName())
            .append("priority", getPriority())
            .append("moduleCode", getModuleCode())
            .append("systemId", getSystemId())
            .append("systemName", getSystemName())
            .append("submitTime", getSubmitTime())
            .append("status", getStatus())
            .append("workItemId", getWorkItemId())
            .toString();
    }
}
