package com.ruoyi.devops.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 运维知识库对象 ops_knowledge
 * 
 * @author ruoyi
 * @date 2026-05-30
 */
public class OpsKnowledge extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 系统ID */
    @Excel(name = "系统ID")
    private Long systemId;

    /** 故障ID */
    @Excel(name = "故障ID")
    private Long faultId;

    /** 标题 */
    @Excel(name = "标题")
    private String title;

    /** 分类 */
    @Excel(name = "分类")
    private String category;

    /** 标签 */
    @Excel(name = "标签")
    private String tags;

    /** 问题描述 */
    private String problemDesc;

    /** 解决方案描述 */
    private String solutionDesc;

    /** 适用环境 */
    @Excel(name = "适用环境")
    private String applicableEnv;

    /** 作者ID */
    @Excel(name = "作者ID")
    private Long authorId;

    /** 浏览次数 */
    @Excel(name = "浏览次数")
    private Integer viewCount;

    /** 发布状态 */
    @Excel(name = "发布状态")
    private String isPublished;

    public void setId(Long id) 
    {
        this.id = id;
    }

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

    public void setFaultId(Long faultId) 
    {
        this.faultId = faultId;
    }

    public Long getFaultId() 
    {
        return faultId;
    }

    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }

    public void setCategory(String category) 
    {
        this.category = category;
    }

    public String getCategory() 
    {
        return category;
    }

    public void setTags(String tags) 
    {
        this.tags = tags;
    }

    public String getTags() 
    {
        return tags;
    }

    public void setProblemDesc(String problemDesc) 
    {
        this.problemDesc = problemDesc;
    }

    public String getProblemDesc() 
    {
        return problemDesc;
    }

    public void setSolutionDesc(String solutionDesc) 
    {
        this.solutionDesc = solutionDesc;
    }

    public String getSolutionDesc() 
    {
        return solutionDesc;
    }

    public void setApplicableEnv(String applicableEnv) 
    {
        this.applicableEnv = applicableEnv;
    }

    public String getApplicableEnv() 
    {
        return applicableEnv;
    }

    public void setAuthorId(Long authorId) 
    {
        this.authorId = authorId;
    }

    public Long getAuthorId() 
    {
        return authorId;
    }

    public void setViewCount(Integer viewCount) 
    {
        this.viewCount = viewCount;
    }

    public Integer getViewCount() 
    {
        return viewCount;
    }

    public void setIsPublished(String isPublished) 
    {
        this.isPublished = isPublished;
    }

    public String getIsPublished() 
    {
        return isPublished;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("systemId", getSystemId())
            .append("faultId", getFaultId())
            .append("title", getTitle())
            .append("category", getCategory())
            .append("tags", getTags())
            .append("problemDesc", getProblemDesc())
            .append("solutionDesc", getSolutionDesc())
            .append("applicableEnv", getApplicableEnv())
            .append("authorId", getAuthorId())
            .append("viewCount", getViewCount())
            .append("isPublished", getIsPublished())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
