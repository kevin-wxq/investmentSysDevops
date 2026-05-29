package com.ruoyi.devops.service.impl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.devops.domain.OpsTeamMember;
import com.ruoyi.devops.mapper.OpsTeamMemberMapper;
import com.ruoyi.devops.service.IOpsTeamMemberService;
@Service
public class OpsTeamMemberServiceImpl implements IOpsTeamMemberService {
    @Autowired
    private OpsTeamMemberMapper teamMemberMapper;
    @Override public List<OpsTeamMember> selectOpsTeamMemberList(OpsTeamMember teamMember) { return teamMemberMapper.selectOpsTeamMemberList(teamMember); }
    @Override public OpsTeamMember selectOpsTeamMemberById(Long id) { return teamMemberMapper.selectOpsTeamMemberById(id); }
    @Override
    public String generateEmployeeNo() {
        String maxNo = teamMemberMapper.selectMaxEmployeeNo();
        int next = parseSequence(maxNo) + 1;
        return String.format("OPS-%04d", next);
    }
    @Override public synchronized int insertOpsTeamMember(OpsTeamMember teamMember) {
        teamMember.setEmployeeNo(generateEmployeeNo());
        if (StringUtils.isEmpty(teamMember.getRoleType())) {
            teamMember.setRoleType("3");
        }
        if (StringUtils.isEmpty(teamMember.getIsOnJob())) {
            teamMember.setIsOnJob("1");
        }
        if (teamMember.getOrderNum() == null) {
            teamMember.setOrderNum(0);
        }
        teamMember.setCreateTime(DateUtils.getNowDate());
        return teamMemberMapper.insertOpsTeamMember(teamMember);
    }
    @Override public int updateOpsTeamMember(OpsTeamMember teamMember) {
        teamMember.setUpdateTime(DateUtils.getNowDate());
        return teamMemberMapper.updateOpsTeamMember(teamMember);
    }
    @Override public int deleteOpsTeamMemberByIds(Long[] ids) { return teamMemberMapper.deleteOpsTeamMemberByIds(ids); }

    private int parseSequence(String employeeNo) {
        if (StringUtils.isEmpty(employeeNo) || !employeeNo.contains("-")) {
            return 0;
        }
        try {
            return Integer.parseInt(employeeNo.substring(employeeNo.lastIndexOf("-") + 1));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
