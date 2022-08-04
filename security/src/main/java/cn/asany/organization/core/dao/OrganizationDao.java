package cn.asany.organization.core.dao;

import cn.asany.organization.core.domain.Organization;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 组织
 *
 * @author limaofeng
 * @version V1.0
 * @date 2022/7/28 9:12 9:12
 */
@Repository
public interface OrganizationDao extends JpaRepository<Organization, Long> {}
