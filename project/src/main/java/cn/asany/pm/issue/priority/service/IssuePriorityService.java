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
package cn.asany.pm.issue.priority.service;

import cn.asany.pm.issue.priority.dao.IssuePriorityDao;
import cn.asany.pm.issue.priority.domain.Priority;
import java.util.List;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Service
public class IssuePriorityService {
  @Autowired private IssuePriorityDao issuePriorityDao;

  public Priority save(Priority priority) {
    return issuePriorityDao.save(priority);
  }

  /**
   * 更新优先级
   *
   * @param id
   * @param merge
   * @param priority
   * @return
   */
  @Transactional
  public Priority updateIssuePriority(Long id, Boolean merge, Priority priority) {
    priority.setId(id);
    return issuePriorityDao.update(priority, merge);
  }

  /** 删除优先级 */
  public Boolean removeIssuePriority(Long id) {
    issuePriorityDao.deleteById(id);
    return true;
  }

  public Page<Priority> findPage(Pageable pager, PropertyFilter filter) {
    return issuePriorityDao.findPage(pager, filter);
  }

  public List<Priority> findAll() {
    return issuePriorityDao.findAll();
  }

  public Priority findById(Long id) {
    return issuePriorityDao.findById(id).orElse(null);
  }
}
