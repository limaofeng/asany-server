package cn.asany.organization.core.dao;

import cn.asany.organization.core.domain.EmployeeGroupScope;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeGroupScopeDao extends AnyJpaRepository<EmployeeGroupScope, String> {}
