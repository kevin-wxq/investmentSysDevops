package com.ruoyi.devops.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 值班日志对象 ops_shift_log
 * 
 * @author ruoyi
 * @date 2026-05-30
 */
public class OpsShiftLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 班次ID */
    @Excel(name = "班次ID")
    private Long shiftId;

    /** 日志时间 */
    @Excel(name = "日志时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date logTime;

    /** 日志类型(1日常 2事件 3告警 4交接) */
    @Excel(name = "日志类型")
    private String logType;

    /** 日志内容 */
    @Excel(name = "日志内容")
    private String logContent;

    /** 严重程度(1普通 2重要 3紧急) */
    @Excel(name = "严重程度")
    private String severity;

    /** 处理人ID */
    private Long handlerId;

    /** 处理人姓名 */
    @Excel(name = "处理人")
    private String handlerName;

    /** 处理结果 */
    @Excel(name = "处理结果")
    private String handlingResult;

    /** 是否升级(0否 1是) */
    @Excel(name = "是否升级")
    private String isEscalated;

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

    public void setShiftId(Long shiftId) 
    {
        this.shiftId = shiftId;
    }

    public Long getShiftId() 
    {
        return shiftId;
    }

    public void setLogTime(Date logTime) 
    {
        this.logTime = logTime;
    }

    public Date getLogTime() 
    {
        return logTime;
    }

    public void setLogType(String logType) 
    {
        this.logType = logType;
    }

    public String getLogType() 
    {
        return logType;
    }

    public void setLogContent(String logContent) 
    {
        this.logContent = logContent;
    }

    public String getLogContent() 
    {
        return logContent;
    }

    public void setSeverity(String severity) 
    {
        this.severity = severity;
    }

    public String getSeverity() 
    {
        return severity;
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

    public void setHandlingResult(String handlingResult) 
    {
        this.handlingResult = handlingResult;
    }

    public String getHandlingResult() 
    {
        return handlingResult;
    }

    public void setIsEscalated(String isEscalated) 
    {
        this.isEscalated = isEscalated;
    }

    public String getIsEscalated() 
    {
        return isEscalated;
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
            .append("shiftId", getShiftId())
            .append("logTime", getLogTime())
            .append("logType", getLogType())
            .append("logContent", getLogContent())
            .append("severity", getSeverity())
            .append("handlerId", getHandlerId())
            .append("handlerName", getHandlerName())
            .append("handlingResult", getHandlingResult())
            .append("isEscalated", getIsEscalated())
            .append("delFlag", getDelFlag())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
