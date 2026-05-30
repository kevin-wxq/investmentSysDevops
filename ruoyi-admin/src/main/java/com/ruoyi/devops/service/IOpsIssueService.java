package com.ruoyi.devops.service;

import java.util.List;
import com.ruoyi.devops.domain.HtBugRecord;
import com.ruoyi.devops.domain.HtRequirement;
import com.ruoyi.devops.domain.OpsIssue;

/**
 * 运维问题Service接口
 *
 * @author ruoyi
 * @date 2026-05-30
 */
public interface IOpsIssueService
{
    public OpsIssue selectOpsIssueById(Long id);

    public List<OpsIssue> selectOpsIssueList(OpsIssue opsIssue);

    public String generateIssueNo();

    public int insertOpsIssue(OpsIssue opsIssue);

    public int updateOpsIssue(OpsIssue opsIssue);

    public int deleteOpsIssueByIds(Long[] ids);

    public int deleteOpsIssueById(Long id);

    public HtBugRecord convertToBug(Long id);

    public HtRequirement convertToRequirement(Long id);
}
