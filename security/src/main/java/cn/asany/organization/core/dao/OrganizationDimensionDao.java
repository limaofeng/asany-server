package cn.asany.organization.core.dao;

import cn.asany.organization.core.bean.OrganizationDimension;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 组织纬度
 *
 * @author limaofeng
 */
@Repository
public interface OrganizationDimensionDao extends JpaRepository<OrganizationDimension, Long> {}
