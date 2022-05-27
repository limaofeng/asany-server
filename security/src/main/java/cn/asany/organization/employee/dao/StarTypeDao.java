package cn.asany.organization.employee.dao;

import cn.asany.organization.employee.domain.StarType;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-06-25 13:02
 */
@Repository
public interface StarTypeDao extends JpaRepository<StarType, String> {}
