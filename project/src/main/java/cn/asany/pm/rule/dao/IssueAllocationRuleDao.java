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
package cn.asany.pm.rule.dao;

import cn.asany.pm.rule.bean.IssueAllocationRule;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Repository
public interface IssueAllocationRuleDao extends AnyJpaRepository<IssueAllocationRule, Long> {

  // 向上排序
  @Modifying
  @Query(
      value =
          "UPDATE gd_issue_allocation_rule t  SET t.priority=t.priority+1  WHERE    t.priority<?1 and t.priority>=?2 ",
      nativeQuery = true)
  Integer rise(Long old, Long now);

  // 向下排序
  @Modifying
  @Query(
      value =
          "UPDATE gd_issue_allocation_rule t  SET t.priority=t.priority-1  WHERE    t.priority>?1 and t.priority<=?2 ",
      nativeQuery = true)
  Integer decline(Long old, Long now);

  // 重新排序
  @Modifying
  @Query(
      value =
          "UPDATE gd_issue_allocation_rule t  SET t.priority=t.priority-1  WHERE    t.priority>?1",
      nativeQuery = true)
  void resetSort(Long priority);
}
