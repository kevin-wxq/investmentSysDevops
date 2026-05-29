package com.ruoyi.devops.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class OpsVendorContact extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long id;

    /** 系统资产ID */
    private Long systemId;

    @Excel(name = "供应商名称")
    private String vendorName;

    @Excel(name = "联系人")
    private String contactPerson;

    @Excel(name = "联系电话")
    private String contactPhone;

    @Excel(name = "邮箱")
    private String contactEmail;

    @Excel(name = "服务范围")
    private String serviceScope;

    @Excel(name = "合同编号")
    private String contractNo;

    @Excel(name = "合同开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    private java.util.Date contractStart;

    @Excel(name = "合同结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private java.util.Date contractEnd;

    @Excel(name = "SLA等级")
    private String slaLevel;

    /** 显示顺序 */
    private Integer orderNum;

    public void setId(Long id) { this.id = id; }
    public Long getId() { return id; }

    public void setSystemId(Long systemId) { this.systemId = systemId; }
    public Long getSystemId() { return systemId; }

    public void setVendorName(String vendorName) { this.vendorName = vendorName; }
    public String getVendorName() { return vendorName; }

    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
    public String getContactPerson() { return contactPerson; }

    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public String getContactPhone() { return contactPhone; }

    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
    public String getContactEmail() { return contactEmail; }

    public void setServiceScope(String serviceScope) { this.serviceScope = serviceScope; }
    public String getServiceScope() { return serviceScope; }

    public void setContractNo(String contractNo) { this.contractNo = contractNo; }
    public String getContractNo() { return contractNo; }

    public void setContractStart(java.util.Date contractStart) { this.contractStart = contractStart; }
    public java.util.Date getContractStart() { return contractStart; }

    public void setContractEnd(java.util.Date contractEnd) { this.contractEnd = contractEnd; }
    public java.util.Date getContractEnd() { return contractEnd; }

    public void setSlaLevel(String slaLevel) { this.slaLevel = slaLevel; }
    public String getSlaLevel() { return slaLevel; }

    public void setOrderNum(Integer orderNum) { this.orderNum = orderNum; }
    public Integer getOrderNum() { return orderNum; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("systemId", getSystemId())
            .append("vendorName", getVendorName())
            .append("contactPerson", getContactPerson())
            .append("contactPhone", getContactPhone())
            .append("contactEmail", getContactEmail())
            .append("serviceScope", getServiceScope())
            .append("contractNo", getContractNo())
            .append("contractStart", getContractStart())
            .append("contractEnd", getContractEnd())
            .append("slaLevel", getSlaLevel())
            .append("orderNum", getOrderNum())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
