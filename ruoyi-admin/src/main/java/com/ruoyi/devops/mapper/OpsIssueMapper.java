package com.ruoyi.devops.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.devops.domain.OpsIssue;

/**
 * 运维问题Mapper接口
 *
 * @author ruoyi
 * @date 2026-05-30
 */
public interface OpsIssueMapper
{
    public OpsIssue selectOpsIssueById(Long id);

    public List<OpsIssue> selectOpsIssueList(OpsIssue opsIssue);

    public String selectMaxIssueNoByPrefix(@Param("prefix") String prefix);

    public int insertOpsIssue(OpsIssue opsIssue);

    public int updateOpsIssue(OpsIssue opsIssue);

    public int deleteOpsIssueById(Long id);

    public int deleteOpsIssueByIds(Long[] ids);
}
