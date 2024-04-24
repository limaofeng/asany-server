package cn.asany.organization.employee.dao;

import cn.asany.organization.employee.domain.StarType;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2022/7/28 9:12 9:12
 */
@Repository
public interface StarTypeDao extends AnyJpaRepository<StarType, String> {}
