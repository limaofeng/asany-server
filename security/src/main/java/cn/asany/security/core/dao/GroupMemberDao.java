package cn.asany.security.core.dao;

import cn.asany.security.core.domain.GroupMember;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupMemberDao extends AnyJpaRepository<GroupMember, Long> {}
