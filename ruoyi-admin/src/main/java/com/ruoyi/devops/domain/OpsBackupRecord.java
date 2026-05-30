package com.ruoyi.devops.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 备份记录对象 ops_backup_record
 * 
 * @author ruoyi
 * @date 2026-05-30
 */
public class OpsBackupRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    private Long issueId;

    /** 系统ID */
    @Excel(name = "系统ID")
    private Long systemId;

    /** 备份类型 */
    @Excel(name = "备份类型")
    private String backupType;

    /** 备份时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "备份时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date backupTime;

    /** 备份大小(MB) */
    @Excel(name = "备份大小(MB)")
    private Integer backupSizeMb;

    /** 备份结果 */
    @Excel(name = "备份结果")
    private String backupResult;

    /** 备份路径 */
    @Excel(name = "备份路径")
    private String backupPath;

    /** 恢复验证 */
    @Excel(name = "恢复验证")
    private String restoreTested;

    /** 执行人ID */
    @Excel(name = "执行人ID")
    private Long executorId;

    /** 错误信息 */
    private String errorMsg;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getIssueId() { return issueId; }
    public void setIssueId(Long issueId) { this.issueId = issueId; }
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

    public void setBackupType(String backupType) 
    {
        this.backupType = backupType;
    }

    public String getBackupType() 
    {
        return backupType;
    }

    public void setBackupTime(Date backupTime) 
    {
        this.backupTime = backupTime;
    }

    public Date getBackupTime() 
    {
        return backupTime;
    }

    public void setBackupSizeMb(Integer backupSizeMb) 
    {
        this.backupSizeMb = backupSizeMb;
    }

    public Integer getBackupSizeMb() 
    {
        return backupSizeMb;
    }

    public void setBackupResult(String backupResult) 
    {
        this.backupResult = backupResult;
    }

    public String getBackupResult() 
    {
        return backupResult;
    }

    public void setBackupPath(String backupPath) 
    {
        this.backupPath = backupPath;
    }

    public String getBackupPath() 
    {
        return backupPath;
    }

    public void setRestoreTested(String restoreTested) 
    {
        this.restoreTested = restoreTested;
    }

    public String getRestoreTested() 
    {
        return restoreTested;
    }

    public void setExecutorId(Long executorId) 
    {
        this.executorId = executorId;
    }

    public Long getExecutorId() 
    {
        return executorId;
    }

    public void setErrorMsg(String errorMsg) 
    {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() 
    {
        return errorMsg;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("systemId", getSystemId())
            .append("backupType", getBackupType())
            .append("backupTime", getBackupTime())
            .append("backupSizeMb", getBackupSizeMb())
            .append("backupResult", getBackupResult())
            .append("backupPath", getBackupPath())
            .append("restoreTested", getRestoreTested())
            .append("executorId", getExecutorId())
            .append("errorMsg", getErrorMsg())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
