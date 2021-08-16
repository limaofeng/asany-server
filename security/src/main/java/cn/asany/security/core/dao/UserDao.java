package cn.asany.security.core.dao;

import cn.asany.security.core.bean.User;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/** @author limaofeng */
@Repository("fantasy.auth.hibernate.UserDao")
public interface UserDao extends JpaRepository<User, Long> {

  //    User findByEmployee(@Param("employee") Employee employee);

  User findByUsername(String name);

  @Query(value = "select gen_value from sys_sequence where gen_name = 'yk_id'", nativeQuery = true)
  long getTouristId();

  @Modifying
  @Query(
      value = "UPDATE sys_sequence SET gen_value = gen_value+1 WHERE gen_name = 'yk_id'",
      nativeQuery = true)
  int updateTouristId();
}
