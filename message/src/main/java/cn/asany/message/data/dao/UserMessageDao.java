package cn.asany.message.data.dao;

import cn.asany.message.data.domain.UserMessage;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 用户消息数据访问层
 *
 * @author limaofeng
 */
@Repository
public interface UserMessageDao extends JpaRepository<UserMessage, Long> {}
