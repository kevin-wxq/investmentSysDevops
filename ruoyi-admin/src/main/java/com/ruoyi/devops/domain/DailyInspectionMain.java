package com.ruoyi.devops.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class DailyInspectionMain extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long id;
    @Excel(name = "巡检日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date inspectionDate;
    @Excel(name = "巡检时间")
    private String inspectionTime;
    @Excel(name = "巡检人ID")
    private Long inspectorId;
    @Excel(name = "巡检人")
    private String inspectorName;
    @Excel(name = "系统ID")
    private Long systemId;
    @Excel(name = "系统名称")
    private String systemName;
    @Excel(name = "服务器IP")
    private String serverIp;
    @Excel(name = "CPU使用率")
    private String cpuUsage;
    @Excel(name = "内存使用率")
    private String memoryUsage;
    @Excel(name = "磁盘使用率")
    private String diskUsage;
    @Excel(name = "网络状态")
    private String networkStatus;
    @Excel(name = "进程状态")
    private String processStatus;
    @Excel(name = "告警信息")
    private String alarmInfo;
    @Excel(name = "告警数量")
    private Integer alarmCount;
    @Excel(name = "巡检结果")
    private String inspectionResult;
    @Excel(name = "处理措施")
    private String handleMeasures;
    @Excel(name = "处理人ID")
    private Long handlerId;
    @Excel(name = "处理人")
    private String handlerName;
    @Excel(name = "处理时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date handleTime;
    @Excel(name = "处理结果")
    private String handleResult;
    private String delFlag;

    public void setId(Long id) { this.id = id; }
    public Long getId() { return id; }
    public void setInspectionDate(Date inspectionDate) { this.inspectionDate = inspectionDate; }
    public Date getInspectionDate() { return inspectionDate; }
    public void setInspectionTime(String inspectionTime) { this.inspectionTime = inspectionTime; }
    public String getInspectionTime() { return inspectionTime; }
    public void setInspectorId(Long inspectorId) { this.inspectorId = inspectorId; }
    public Long getInspectorId() { return inspectorId; }
    public void setInspectorName(String inspectorName) { this.inspectorName = inspectorName; }
    public String getInspectorName() { return inspectorName; }
    public void setSystemId(Long systemId) { this.systemId = systemId; }
    public Long getSystemId() { return systemId; }
    public void setSystemName(String systemName) { this.systemName = systemName; }
    public String getSystemName() { return systemName; }
    public void setServerIp(String serverIp) { this.serverIp = serverIp; }
    public String getServerIp() { return serverIp; }
    public void setCpuUsage(String cpuUsage) { this.cpuUsage = cpuUsage; }
    public String getCpuUsage() { return cpuUsage; }
    public void setMemoryUsage(String memoryUsage) { this.memoryUsage = memoryUsage; }
    public String getMemoryUsage() { return memoryUsage; }
    public void setDiskUsage(String diskUsage) { this.diskUsage = diskUsage; }
    public String getDiskUsage() { return diskUsage; }
    public void setNetworkStatus(String networkStatus) { this.networkStatus = networkStatus; }
    public String getNetworkStatus() { return networkStatus; }
    public void setProcessStatus(String processStatus) { this.processStatus = processStatus; }
    public String getProcessStatus() { return processStatus; }
    public void setAlarmInfo(String alarmInfo) { this.alarmInfo = alarmInfo; }
    public String getAlarmInfo() { return alarmInfo; }
    public void setAlarmCount(Integer alarmCount) { this.alarmCount = alarmCount; }
    public Integer getAlarmCount() { return alarmCount; }
    public void setInspectionResult(String inspectionResult) { this.inspectionResult = inspectionResult; }
    public String getInspectionResult() { return inspectionResult; }
    public void setHandleMeasures(String handleMeasures) { this.handleMeasures = handleMeasures; }
    public String getHandleMeasures() { return handleMeasures; }
    public void setHandlerId(Long handlerId) { this.handlerId = handlerId; }
    public Long getHandlerId() { return handlerId; }
    public void setHandlerName(String handlerName) { this.handlerName = handlerName; }
    public String getHandlerName() { return handlerName; }
    public void setHandleTime(Date handleTime) { this.handleTime = handleTime; }
    public Date getHandleTime() { return handleTime; }
    public void setHandleResult(String handleResult) { this.handleResult = handleResult; }
    public String getHandleResult() { return handleResult; }
    public void setDelFlag(String delFlag) { this.delFlag = delFlag; }
    public String getDelFlag() { return delFlag; }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("inspectionDate", getInspectionDate())
            .append("inspectionTime", getInspectionTime())
            .append("inspectorId", getInspectorId())
            .append("inspectorName", getInspectorName())
            .append("systemId", getSystemId())
            .append("systemName", getSystemName())
            .append("serverIp", getServerIp())
            .append("cpuUsage", getCpuUsage())
            .append("memoryUsage", getMemoryUsage())
            .append("diskUsage", getDiskUsage())
            .append("networkStatus", getNetworkStatus())
            .append("processStatus", getProcessStatus())
            .append("alarmInfo", getAlarmInfo())
            .append("alarmCount", getAlarmCount())
            .append("inspectionResult", getInspectionResult())
            .append("handleMeasures", getHandleMeasures())
            .append("handlerId", getHandlerId())
            .append("handlerName", getHandlerName())
            .append("handleTime", getHandleTime())
            .append("handleResult", getHandleResult())
            .append("delFlag", getDelFlag())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
