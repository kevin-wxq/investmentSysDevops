package com.ruoyi.devops.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class OpsSystemAsset extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long id;

    @Excel(name = "系统编码")
    private String systemCode;

    @Excel(name = "系统名称")
    private String systemName;

    @Excel(name = "类型")
    private String systemType;

    @Excel(name = "重要等级")
    private String importanceLevel;

    @Excel(name = "访问地址")
    private String systemUrl;

    @Excel(name = "部署IP")
    private String serverIp;

    @Excel(name = "数据库类型")
    private String dbType;

    @Excel(name = "开发语言")
    private String devLang;

    @Excel(name = "所属部门")
    private String department;

    @Excel(name = "业务负责人")
    private String businessOwner;

    /** 技术负责人ID */
    private Long techOwnerId;

    @Excel(name = "上线日期", width = 30, dateFormat = "yyyy-MM-dd")
    private java.util.Date goLiveDate;

    @Excel(name = "状态")
    private String systemStatus;

    /** 显示顺序 */
    private Integer orderNum;

    public void setId(Long id) { this.id = id; }
    public Long getId() { return id; }

    public void setSystemCode(String systemCode) { this.systemCode = systemCode; }
    public String getSystemCode() { return systemCode; }

    public void setSystemName(String systemName) { this.systemName = systemName; }
    public String getSystemName() { return systemName; }

    public void setSystemType(String systemType) { this.systemType = systemType; }
    public String getSystemType() { return systemType; }

    public void setImportanceLevel(String importanceLevel) { this.importanceLevel = importanceLevel; }
    public String getImportanceLevel() { return importanceLevel; }

    public void setSystemUrl(String systemUrl) { this.systemUrl = systemUrl; }
    public String getSystemUrl() { return systemUrl; }

    public void setServerIp(String serverIp) { this.serverIp = serverIp; }
    public String getServerIp() { return serverIp; }

    public void setDbType(String dbType) { this.dbType = dbType; }
    public String getDbType() { return dbType; }

    public void setDevLang(String devLang) { this.devLang = devLang; }
    public String getDevLang() { return devLang; }

    public void setDepartment(String department) { this.department = department; }
    public String getDepartment() { return department; }

    public void setBusinessOwner(String businessOwner) { this.businessOwner = businessOwner; }
    public String getBusinessOwner() { return businessOwner; }

    public void setTechOwnerId(Long techOwnerId) { this.techOwnerId = techOwnerId; }
    public Long getTechOwnerId() { return techOwnerId; }

    public void setGoLiveDate(java.util.Date goLiveDate) { this.goLiveDate = goLiveDate; }
    public java.util.Date getGoLiveDate() { return goLiveDate; }

    public void setSystemStatus(String systemStatus) { this.systemStatus = systemStatus; }
    public String getSystemStatus() { return systemStatus; }

    public void setOrderNum(Integer orderNum) { this.orderNum = orderNum; }
    public Integer getOrderNum() { return orderNum; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("systemCode", getSystemCode())
            .append("systemName", getSystemName())
            .append("systemType", getSystemType())
            .append("importanceLevel", getImportanceLevel())
            .append("systemUrl", getSystemUrl())
            .append("serverIp", getServerIp())
            .append("dbType", getDbType())
            .append("devLang", getDevLang())
            .append("department", getDepartment())
            .append("businessOwner", getBusinessOwner())
            .append("techOwnerId", getTechOwnerId())
            .append("goLiveDate", getGoLiveDate())
            .append("systemStatus", getSystemStatus())
            .append("orderNum", getOrderNum())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
