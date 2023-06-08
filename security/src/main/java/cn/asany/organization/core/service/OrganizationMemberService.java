package cn.asany.organization.core.service;

import cn.asany.organization.core.dao.OrganizationMemberDao;
import cn.asany.organization.core.domain.OrganizationMember;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrganizationMemberService {

  private final OrganizationMemberDao organizationMemberDao;

  public OrganizationMemberService(OrganizationMemberDao organizationMemberDao) {
    this.organizationMemberDao = organizationMemberDao;
  }

  @Transactional(readOnly = true)
  public Optional<OrganizationMember> findOneByUserAndOrganization(Long user, Long organization) {
    return this.organizationMemberDao.findOne(
        PropertyFilter.newFilter().equal("user.id", user).equal("organization.id", organization));
  }
}
