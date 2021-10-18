package cn.asany.organization.employee.dao;

import cn.asany.organization.employee.bean.Employee;
import java.util.List;
import java.util.Map;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author 李茂峰 @Version V1.0
 * @data 2019/3/5 15:33
 */
@Repository
public interface EmployeeDao extends JpaRepository<Employee, Long> {

  Employee findByJobNumber(String jobNumber);

  List<Employee> findByNameAndJobNumber(String name, String jopName);

  @Query(nativeQuery = true, value = "SELECT employee FROM dj_apply")
  List<Long> getDjApplyEidList();

  @Query(nativeQuery = true, value = "select sn from org_employee ")
  List<String> getBySns();

  @Query("select e.id from Employee as e where date_format(e.birthday,'%m%d') = :birth")
  List<Long> findBirthEmployeeIds(String birth);

  @Query(
      nativeQuery = true,
      value =
          "SELECT e.id, e.name, h.employee_id as zbsjId, i.organization_id, i.status_id FROM org_employee AS e"
              + " LEFT JOIN org_organization_employee AS c ON e.id = c.employee_id"
              + " LEFT JOIN org_employee_position as f on e.id = f.employee_id"
              + " LEFT JOIN org_position as g on f.department_id = g.department_id and g.organization_id = 'fkdzz' and g.name = '支部书记'"
              + " LEFT JOIN org_employee_position as h on g.id = h.position_id"
              + " LEFT JOIN org_organization_employee AS i ON h.employee_id = i.employee_id"
              + " WHERE date_format( e.birthday, '%m%d' ) = :birth AND c.organization_id = 'fkdzz' AND c.status_id not IN ( '10010', '10011', '10012', '10015', '10017' )")
  List<Map<String, Object>> findBirthEmployees(String birth);

  @Query(
      nativeQuery = true,
      value =
          "SELECT e.id, e.name, a.value, h.employee_id as zbsjId, i.organization_id, i.status_id FROM org_employee AS e"
              + " LEFT JOIN org_organization_employee AS c ON e.id = c.employee_id"
              + " LEFT JOIN org_employee_field_value AS a ON e.id = a.employee_id"
              + " LEFT JOIN org_employee_position AS f ON e.id = f.employee_id"
              + " LEFT JOIN org_position AS g ON f.department_id = g.department_id AND g.organization_id = 'fkdzz' AND g.NAME = '支部书记'"
              + " LEFT JOIN org_employee_position AS h ON g.id = h.position_id"
              + " LEFT JOIN org_organization_employee AS i ON h.employee_id = i.employee_id"
              + " WHERE date_format( a.VALUE, '%m%d' ) = :birth and a.employee_field_id = '103' AND c.organization_id = 'fkdzz' AND c.status_id not IN ( '10010', '10011', '10012', '10015', '10017' )")
  List<Map<String, Object>> findPoliticsBirthEmployees(String birth);
}
