package cn.asany.organization.core.dao;

import cn.asany.organization.core.bean.DepartmentType;
import cn.asany.organization.core.bean.Organization;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentTypeDao extends JpaRepository<DepartmentType, Long> {

    List<DepartmentType> findByOrganization(Organization organization);

}
