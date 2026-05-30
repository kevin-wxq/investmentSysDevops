package com.ruoyi.devops.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 事项流转日志对象 ops_work_item_log
 *
 * @author ruoyi
 * @date 2026-05-30
 */
public class OpsWorkItemLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long id;

    /** 工作项ID */
    private Long workItemId;

    /** 事项编号 */
    @Excel(name = "事项编号")
    private String itemNo;

    /** 原状态 */
    @Excel(name = "原状态")
    private String fromStatus;

    /** 新状态 */
    @Excel(name = "新状态")
    private String toStatus;

    /** 动作名称 */
    @Excel(name = "动作名称")
    private String actionName;

    /** 操作人 */
    @Excel(name = "操作人")
    private String operatorName;

    /** 操作时间 */
    @Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date actionTime;

    /** 操作备注 */
    @Excel(name = "操作备注")
    private String actionRemark;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getWorkItemId() { return workItemId; }
    public void setWorkItemId(Long workItemId) { this.workItemId = workItemId; }
    public String getItemNo() { return itemNo; }
    public void setItemNo(String itemNo) { this.itemNo = itemNo; }
    public String getFromStatus() { return fromStatus; }
    public void setFromStatus(String fromStatus) { this.fromStatus = fromStatus; }
    public String getToStatus() { return toStatus; }
    public void setToStatus(String toStatus) { this.toStatus = toStatus; }
    public String getActionName() { return actionName; }
    public void setActionName(String actionName) { this.actionName = actionName; }
    public String getOperatorName() { return operatorName; }
    public void setOperatorName(String operatorName) { this.operatorName = operatorName; }
    public Date getActionTime() { return actionTime; }
    public void setActionTime(Date actionTime) { this.actionTime = actionTime; }
    public String getActionRemark() { return actionRemark; }
    public void setActionRemark(String actionRemark) { this.actionRemark = actionRemark; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("workItemId", getWorkItemId())
            .append("itemNo", getItemNo())
            .append("fromStatus", getFromStatus())
            .append("toStatus", getToStatus())
            .append("actionName", getActionName())
            .append("operatorName", getOperatorName())
            .append("actionTime", getActionTime())
            .append("actionRemark", getActionRemark())
            .toString();
    }
}
