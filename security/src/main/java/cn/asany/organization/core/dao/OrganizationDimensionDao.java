package cn.asany.organization.core.dao;

import cn.asany.organization.core.domain.OrganizationDimension;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 组织纬度
 *
 * @author limaofeng
 */
@Repository
public interface OrganizationDimensionDao extends AnyJpaRepository<OrganizationDimension, Long> {}
