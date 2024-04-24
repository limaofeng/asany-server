package cn.asany.organization.employee.dao;

import cn.asany.organization.employee.domain.Employee;
import cn.asany.organization.employee.domain.Star;
import cn.asany.organization.employee.domain.StarType;
import java.util.List;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2022/7/28 9:12 9:12
 */
@Repository
public interface StarDao extends AnyJpaRepository<Star, Long> {

  @Query(
      nativeQuery = true,
      value = "select * from  org_star where employee_id = :id and type = :type")
  List<Star> findByStargazerAndStarType(@Param("id") Long id, @Param("type") String type);

  Star findByStargazerAndStarTypeAndGalaxy(Employee stargazer, StarType starType, String galaxy);
}
