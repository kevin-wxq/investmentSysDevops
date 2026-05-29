package com.ruoyi.devops.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class OpsTeamMember extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long id;

    /** 用户ID */
    private Long userId;

    @Excel(name = "姓名")
    private String realName;

    @Excel(name = "工号")
    private String employeeNo;

    @Excel(name = "手机号")
    private String mobile;

    @Excel(name = "邮箱")
    private String email;

    @Excel(name = "所属小组")
    private String team;

    @Excel(name = "角色")
    private String roleType;

    @Excel(name = "技能标签")
    private String skillTags;

    @Excel(name = "在岗状态")
    private String isOnJob;

    @Excel(name = "入职日期", width = 30, dateFormat = "yyyy-MM-dd")
    private java.util.Date entryDate;

    /** 显示顺序 */
    private Integer orderNum;

    public void setId(Long id) { this.id = id; }
    public Long getId() { return id; }

    public void setUserId(Long userId) { this.userId = userId; }
    public Long getUserId() { return userId; }

    public void setRealName(String realName) { this.realName = realName; }
    public String getRealName() { return realName; }

    public void setEmployeeNo(String employeeNo) { this.employeeNo = employeeNo; }
    public String getEmployeeNo() { return employeeNo; }

    public void setMobile(String mobile) { this.mobile = mobile; }
    public String getMobile() { return mobile; }

    public void setEmail(String email) { this.email = email; }
    public String getEmail() { return email; }

    public void setTeam(String team) { this.team = team; }
    public String getTeam() { return team; }

    public void setRoleType(String roleType) { this.roleType = roleType; }
    public String getRoleType() { return roleType; }

    public void setSkillTags(String skillTags) { this.skillTags = skillTags; }
    public String getSkillTags() { return skillTags; }

    public void setIsOnJob(String isOnJob) { this.isOnJob = isOnJob; }
    public String getIsOnJob() { return isOnJob; }

    public void setEntryDate(java.util.Date entryDate) { this.entryDate = entryDate; }
    public java.util.Date getEntryDate() { return entryDate; }

    public void setOrderNum(Integer orderNum) { this.orderNum = orderNum; }
    public Integer getOrderNum() { return orderNum; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("realName", getRealName())
            .append("employeeNo", getEmployeeNo())
            .append("mobile", getMobile())
            .append("email", getEmail())
            .append("team", getTeam())
            .append("roleType", getRoleType())
            .append("skillTags", getSkillTags())
            .append("isOnJob", getIsOnJob())
            .append("entryDate", getEntryDate())
            .append("orderNum", getOrderNum())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
