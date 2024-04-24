package cn.asany.organization.core.dao;

import cn.asany.organization.core.domain.DepartmentType;
import cn.asany.organization.core.domain.Organization;
import java.util.List;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentTypeDao extends AnyJpaRepository<DepartmentType, Long> {

  List<DepartmentType> findByOrganization(Organization organization);
}
