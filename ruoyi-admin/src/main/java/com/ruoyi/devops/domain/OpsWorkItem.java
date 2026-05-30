package com.ruoyi.devops.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 统一闭环事项对象 ops_work_item
 *
 * @author ruoyi
 * @date 2026-05-30
 */
public class OpsWorkItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 事项编号 */
    @Excel(name = "事项编号")
    private String itemNo;

    /** 事项类型 */
    @Excel(name = "事项类型")
    private String itemType;

    /** 来源模块 */
    @Excel(name = "来源模块")
    private String sourceModule;

    /** 来源记录ID */
    private Long sourceId;

    /** 系统ID */
    private Long systemId;

    /** 系统名称 */
    @Excel(name = "系统名称")
    private String systemName;

    /** 标题 */
    @Excel(name = "标题")
    private String title;

    /** 优先级 */
    @Excel(name = "优先级")
    private String priority;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 负责人ID */
    private Long ownerId;

    /** 负责人 */
    @Excel(name = "负责人")
    private String ownerName;

    /** 提出人 */
    @Excel(name = "提出人")
    private String submitter;

    /** 提出时间 */
    @Excel(name = "提出时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date submitTime;

    /** 计划完成时间 */
    @Excel(name = "计划完成时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date planFinishTime;

    /** 实际完成时间 */
    @Excel(name = "实际完成时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date actualFinishTime;

    /** 处理进展 */
    @Excel(name = "处理进展")
    private String progress;

    /** 验收结果 */
    @Excel(name = "验收结果")
    private String acceptanceResult;

    /** 关闭说明 */
    @Excel(name = "关闭说明")
    private String closeDesc;

    /** 是否逾期 */
    @Excel(name = "是否逾期")
    private String overdueFlag;

    /** 删除标志 */
    private String delFlag;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getItemNo() { return itemNo; }
    public void setItemNo(String itemNo) { this.itemNo = itemNo; }
    public String getItemType() { return itemType; }
    public void setItemType(String itemType) { this.itemType = itemType; }
    public String getSourceModule() { return sourceModule; }
    public void setSourceModule(String sourceModule) { this.sourceModule = sourceModule; }
    public Long getSourceId() { return sourceId; }
    public void setSourceId(Long sourceId) { this.sourceId = sourceId; }
    public Long getSystemId() { return systemId; }
    public void setSystemId(Long systemId) { this.systemId = systemId; }
    public String getSystemName() { return systemName; }
    public void setSystemName(String systemName) { this.systemName = systemName; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
    public String getSubmitter() { return submitter; }
    public void setSubmitter(String submitter) { this.submitter = submitter; }
    public Date getSubmitTime() { return submitTime; }
    public void setSubmitTime(Date submitTime) { this.submitTime = submitTime; }
    public Date getPlanFinishTime() { return planFinishTime; }
    public void setPlanFinishTime(Date planFinishTime) { this.planFinishTime = planFinishTime; }
    public Date getActualFinishTime() { return actualFinishTime; }
    public void setActualFinishTime(Date actualFinishTime) { this.actualFinishTime = actualFinishTime; }
    public String getProgress() { return progress; }
    public void setProgress(String progress) { this.progress = progress; }
    public String getAcceptanceResult() { return acceptanceResult; }
    public void setAcceptanceResult(String acceptanceResult) { this.acceptanceResult = acceptanceResult; }
    public String getCloseDesc() { return closeDesc; }
    public void setCloseDesc(String closeDesc) { this.closeDesc = closeDesc; }
    public String getOverdueFlag() { return overdueFlag; }
    public void setOverdueFlag(String overdueFlag) { this.overdueFlag = overdueFlag; }
    public String getDelFlag() { return delFlag; }
    public void setDelFlag(String delFlag) { this.delFlag = delFlag; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("itemNo", getItemNo())
            .append("itemType", getItemType())
            .append("sourceModule", getSourceModule())
            .append("sourceId", getSourceId())
            .append("systemId", getSystemId())
            .append("systemName", getSystemName())
            .append("title", getTitle())
            .append("priority", getPriority())
            .append("status", getStatus())
            .append("ownerId", getOwnerId())
            .append("ownerName", getOwnerName())
            .append("submitter", getSubmitter())
            .append("submitTime", getSubmitTime())
            .append("planFinishTime", getPlanFinishTime())
            .append("actualFinishTime", getActualFinishTime())
            .append("progress", getProgress())
            .append("acceptanceResult", getAcceptanceResult())
            .append("closeDesc", getCloseDesc())
            .append("overdueFlag", getOverdueFlag())
            .append("delFlag", getDelFlag())
            .append("remark", getRemark())
            .toString();
    }
}
