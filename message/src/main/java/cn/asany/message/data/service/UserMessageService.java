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
package cn.asany.message.data.service;

import cn.asany.message.data.dao.UserMessageDao;
import cn.asany.message.data.domain.UserMessage;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户消息服务
 *
 * @author limaofeng
 */
@Service
public class UserMessageService {

  private final UserMessageDao userMessageDao;

  public UserMessageService(UserMessageDao userMessageDao) {
    this.userMessageDao = userMessageDao;
  }

  @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRES_NEW)
  public void save(UserMessage userMessage) {
    this.userMessageDao.save(userMessage);
  }

  @Transactional(readOnly = true)
  public Page<UserMessage> findPage(Pageable pageable, PropertyFilter filter) {
    return this.userMessageDao.findPage(pageable, filter);
  }
}
