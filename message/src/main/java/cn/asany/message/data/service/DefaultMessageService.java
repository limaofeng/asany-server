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

import cn.asany.message.api.MessageService;
import cn.asany.message.data.dao.MessageDao;
import cn.asany.message.data.domain.Message;
import cn.asany.message.data.domain.enums.MessageStatus;
import cn.asany.message.define.domain.MessageType;
import cn.asany.message.define.domain.toys.MessageContent;
import cn.asany.message.define.service.MessageTypeService;
import java.util.Map;
import java.util.Optional;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.error.ValidationException;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 消息服务
 *
 * @author limaofeng
 */
@Service
public class DefaultMessageService implements MessageService {

  private final MessageDao messageDao;

  private final MessageTypeService messageTypeService;

  public DefaultMessageService(MessageDao messageDao, MessageTypeService messageTypeService) {
    this.messageDao = messageDao;
    this.messageTypeService = messageTypeService;
  }

  @Override
  public String send(String type, String content, String... receivers) {

    return "";
  }

  @Override
  public String send(String type, String title, String content, String... receivers) {
    return "";
  }

  @Override
  @Transactional(rollbackFor = RuntimeException.class)
  public String send(String type, Map<String, Object> variables, String... receivers) {

    if (receivers.length == 0) {
      throw new ValidationException("接收人不能为空");
    }

    MessageType messageType =
        this.messageTypeService
            .findById(type)
            .orElseThrow(() -> new ValidationException("消息类型不存在"));

    Message message =
        Message.builder()
            .type(messageType)
            .status(MessageStatus.UNSENT)
            .content(MessageContent.empty())
            .recipients(receivers)
            .variables(variables)
            .build();
    this.messageDao.save(message);
    return String.valueOf(message.getId());
  }

  @Transactional(readOnly = true)
  public Optional<Message> findById(Long id) {
    return this.messageDao
        .findById(id)
        .map(
            msg -> {
              msg.getRecipients().forEach(Hibernate::unproxy);
              return msg;
            });
  }

  @Transactional(readOnly = true)
  public Page<Message> findPage(Pageable pageable, PropertyFilter filter) {
    return this.messageDao.findPage(pageable, filter);
  }
}
