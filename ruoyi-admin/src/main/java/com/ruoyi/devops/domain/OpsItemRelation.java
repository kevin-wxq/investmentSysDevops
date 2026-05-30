package com.ruoyi.devops.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 事项关系对象 ops_item_relation
 *
 * @author ruoyi
 * @date 2026-05-30
 */
public class OpsItemRelation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long id;

    @Excel(name = "来源类型")
    private String sourceType;

    private Long sourceId;

    @Excel(name = "来源编号")
    private String sourceNo;

    @Excel(name = "目标类型")
    private String targetType;

    private Long targetId;

    @Excel(name = "目标编号")
    private String targetNo;

    @Excel(name = "关系类型")
    private String relationType;

    @Excel(name = "关系说明")
    private String relationDesc;

    @Excel(name = "关联时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date relationTime;

    private String delFlag;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSourceType() { return sourceType; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }
    public Long getSourceId() { return sourceId; }
    public void setSourceId(Long sourceId) { this.sourceId = sourceId; }
    public String getSourceNo() { return sourceNo; }
    public void setSourceNo(String sourceNo) { this.sourceNo = sourceNo; }
    public String getTargetType() { return targetType; }
    public void setTargetType(String targetType) { this.targetType = targetType; }
    public Long getTargetId() { return targetId; }
    public void setTargetId(Long targetId) { this.targetId = targetId; }
    public String getTargetNo() { return targetNo; }
    public void setTargetNo(String targetNo) { this.targetNo = targetNo; }
    public String getRelationType() { return relationType; }
    public void setRelationType(String relationType) { this.relationType = relationType; }
    public String getRelationDesc() { return relationDesc; }
    public void setRelationDesc(String relationDesc) { this.relationDesc = relationDesc; }
    public Date getRelationTime() { return relationTime; }
    public void setRelationTime(Date relationTime) { this.relationTime = relationTime; }
    public String getDelFlag() { return delFlag; }
    public void setDelFlag(String delFlag) { this.delFlag = delFlag; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("sourceType", getSourceType())
            .append("sourceId", getSourceId())
            .append("sourceNo", getSourceNo())
            .append("targetType", getTargetType())
            .append("targetId", getTargetId())
            .append("targetNo", getTargetNo())
            .append("relationType", getRelationType())
            .append("relationDesc", getRelationDesc())
            .append("relationTime", getRelationTime())
            .append("delFlag", getDelFlag())
            .append("remark", getRemark())
            .toString();
    }
}
