package cn.asany.organization.core.dao;

import cn.asany.organization.core.bean.OrganizationMember;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationMemberDao extends JpaRepository<OrganizationMember, Long> {}
