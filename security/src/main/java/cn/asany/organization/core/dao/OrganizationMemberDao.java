package cn.asany.organization.core.dao;

import cn.asany.organization.core.domain.OrganizationMember;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationMemberDao extends AnyJpaRepository<OrganizationMember, Long> {}
