/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.organization.employee.dao;

import cn.asany.organization.employee.domain.Employee;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 员工
 *
 * @author 李茂峰
 */
@Repository
public interface EmployeeDao extends AnyJpaRepository<Employee, Long> {
  /**
   * 删除员工 - 根据组织 ID
   *
   * @param orgId 组织 ID
   */
  @Modifying
  @Query(nativeQuery = true, value = "DELETE FROM org_employee e WHERE e.organization_id = :orgId")
  void deleteByOrgId(@Param("orgId") Long orgId);
}
