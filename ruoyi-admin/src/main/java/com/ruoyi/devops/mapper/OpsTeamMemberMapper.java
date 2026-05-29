package com.ruoyi.devops.mapper;
import java.util.List;
import com.ruoyi.devops.domain.OpsTeamMember;
public interface OpsTeamMemberMapper {
    public List<OpsTeamMember> selectOpsTeamMemberList(OpsTeamMember teamMember);
    public OpsTeamMember selectOpsTeamMemberById(Long id);
    public String selectMaxEmployeeNo();
    public int insertOpsTeamMember(OpsTeamMember teamMember);
    public int updateOpsTeamMember(OpsTeamMember teamMember);
    public int deleteOpsTeamMemberById(Long id);
    public int deleteOpsTeamMemberByIds(Long[] ids);
}
