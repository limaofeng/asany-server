package cn.asany.message.data.dao;

import cn.asany.message.data.domain.UserMessage;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 用户消息数据访问层
 *
 * @author limaofeng
 */
@Repository
public interface UserMessageDao extends AnyJpaRepository<UserMessage, Long> {}
