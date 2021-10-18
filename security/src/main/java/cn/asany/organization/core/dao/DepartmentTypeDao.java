package cn.asany.organization.core.dao;

import cn.asany.organization.core.bean.DepartmentType;
import cn.asany.organization.core.bean.Organization;
import java.util.List;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentTypeDao extends JpaRepository<DepartmentType, Long> {

  List<DepartmentType> findByOrganization(Organization organization);
}
