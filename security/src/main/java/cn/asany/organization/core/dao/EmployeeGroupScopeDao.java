package cn.asany.organization.core.dao;

import cn.asany.organization.core.domain.EmployeeGroupScope;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeGroupScopeDao extends JpaRepository<EmployeeGroupScope, String> {}
