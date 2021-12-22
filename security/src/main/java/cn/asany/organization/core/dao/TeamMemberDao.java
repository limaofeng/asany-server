package cn.asany.organization.core.dao;

import cn.asany.organization.core.bean.TeamMember;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamMemberDao extends JpaRepository<TeamMember, Long> {}
