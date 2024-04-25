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
package cn.asany.organization.relationship.dao;

import cn.asany.organization.core.domain.Job;
import cn.asany.organization.relationship.domain.Position;
import java.util.List;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2022/7/28 9:12 9:12
 */
@Repository
public interface PositionDao extends AnyJpaRepository<Position, Long> {

  @Modifying
  @Query(nativeQuery = true, value = "delete from org_position where job_id=?1")
  void deleteByJob(@Param("jobId") Long jobId);

  List<Position> findByJob(Job job);

  /**
   * 删除职位 - 根据组织 ID
   *
   * @param orgId 组织 ID
   */
  @Modifying
  @Query(nativeQuery = true, value = "DELETE FROM org_position e WHERE e.organization_id = :orgId")
  void deleteByOrgId(@org.springframework.data.repository.query.Param("orgId") Long orgId);
}
