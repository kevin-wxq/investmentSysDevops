package com.ruoyi.devops.service;
import java.util.List;
import com.ruoyi.devops.domain.OpsTeamMember;
public interface IOpsTeamMemberService {
    public List<OpsTeamMember> selectOpsTeamMemberList(OpsTeamMember teamMember);
    public OpsTeamMember selectOpsTeamMemberById(Long id);
    public String generateEmployeeNo();
    public int insertOpsTeamMember(OpsTeamMember teamMember);
    public int updateOpsTeamMember(OpsTeamMember teamMember);
    public int deleteOpsTeamMemberByIds(Long[] ids);
}
