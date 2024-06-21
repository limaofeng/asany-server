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
package cn.asany.pm.issue.core.dao;

import cn.asany.pm.issue.core.domain.Issue;
import java.util.List;
import java.util.Map;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Repository
public interface IssueDao extends AnyJpaRepository<Issue, Long> {

  @Query(
      nativeQuery = true,
      value =
          "SELECT assignee ,count(id) as count FROM gd_issue WHERE status_id IN('2','3','4') GROUP BY  assignee")
  List<Map<String, Object>> assigneeCount();
}
