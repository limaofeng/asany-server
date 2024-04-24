package cn.asany.organization.core.dao;

import cn.asany.organization.core.domain.Organization;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 组织
 *
 * @author limaofeng
 * @version V1.0
 * @date 2022/7/28 9:12 9:12
 */
@Repository
public interface OrganizationDao extends AnyJpaRepository<Organization, Long> {}
