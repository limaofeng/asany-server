package cn.asany.security.core.dao;

import cn.asany.security.core.domain.User;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author limaofeng
 */
@Repository("fantasy.auth.hibernate.UserDao")
public interface UserDao extends AnyJpaRepository<User, Long> {

  //    User findByEmployee(@Param("employee") Employee employee);

  User findByUsername(String name);
}
