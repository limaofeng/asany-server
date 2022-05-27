package cn.asany.organization.core.dao;

import cn.asany.organization.core.domain.DepartmentLink;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-05-28 14:19
 */
@Repository
public interface DepartmentLinkDao extends JpaRepository<DepartmentLink, Long> {}
