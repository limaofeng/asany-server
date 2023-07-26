package cn.asany.message.data.service;

import cn.asany.message.data.dao.UserMessageDao;
import cn.asany.message.data.domain.UserMessage;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
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
