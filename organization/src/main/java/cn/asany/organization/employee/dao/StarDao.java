package cn.asany.organization.employee.dao;

import cn.asany.organization.employee.bean.Employee;
import cn.asany.organization.employee.bean.Star;
import cn.asany.organization.employee.bean.StarType;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author limaofeng
 * @version V1.0
 * @Description: TODO
 * @date 2019-06-25 13:02
 */
@Repository
public interface StarDao extends JpaRepository<Star, Long> {

    @Query(nativeQuery = true, value = "select * from  org_star where employee_id = :id and type = :type")
    List<Star> findByStargazerAndStarType(@Param("id") Long id, @Param("type") String type);

    Star findByStargazerAndStarTypeAndGalaxy(Employee stargazer, StarType starType, String galaxy);
}
