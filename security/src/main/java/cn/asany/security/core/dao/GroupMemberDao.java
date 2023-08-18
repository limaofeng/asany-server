package cn.asany.security.core.dao;

import cn.asany.security.core.domain.GroupMember;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupMemberDao extends JpaRepository<GroupMember, Long> {}
