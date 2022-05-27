package cn.asany.organization.core.service;

import cn.asany.organization.core.dao.TeamMemberDao;
import cn.asany.organization.core.domain.TeamMember;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamMemberService {

  @Autowired private TeamMemberDao teamMemberDao;

  public List<TeamMember> findAll() {
    return this.teamMemberDao.findAll();
  }
}
