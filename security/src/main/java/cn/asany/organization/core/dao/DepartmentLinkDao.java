package cn.asany.organization.core.dao;

import cn.asany.organization.core.domain.DepartmentLink;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2022/7/28 9:12 9:12
 */
@Repository
public interface DepartmentLinkDao extends AnyJpaRepository<DepartmentLink, Long> {}
