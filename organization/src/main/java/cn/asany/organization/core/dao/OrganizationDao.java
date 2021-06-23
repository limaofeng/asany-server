package cn.asany.organization.core.dao;

import cn.asany.organization.core.bean.Organization;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 组织
 *
 * @author limaofeng
 * @version V1.0
 * @date 2019-04-24 17:00
 */
@Repository
public interface OrganizationDao extends JpaRepository<Organization, Long> {
}
