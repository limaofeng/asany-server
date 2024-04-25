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
package cn.asany.pm.issue.attribute.service;

import cn.asany.pm.issue.attribute.dao.StatusDao;
import cn.asany.pm.issue.attribute.domain.Status;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 状态服务
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class StatusService {

  private final StatusDao statusDao;

  public StatusService(StatusDao statusDao) {
    this.statusDao = statusDao;
  }

  /**
   * 添加状态
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public Status createIssueState(Status issueState) {
    return statusDao.save(issueState);
  }

  /**
   * 修改状态
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public Status updateIssueState(Long id, Boolean merge, Status issueState) {
    issueState.setId(id);
    return statusDao.update(issueState, merge);
  }

  /**
   * 删除状态
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public Boolean removeStatus(Long id) {
    statusDao.deleteById(id);
    return true;
  }

  /**
   * 保存数据
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public Status save(Status Status) {
    return statusDao.save(Status);
  }

  public Page<Status> findPage(Pageable pageable, PropertyFilter filter) {
    return statusDao.findPage(pageable, filter);
  }
}
