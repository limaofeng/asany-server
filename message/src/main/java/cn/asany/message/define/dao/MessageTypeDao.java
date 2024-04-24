package cn.asany.message.define.dao;

import cn.asany.message.define.domain.MessageType;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 消息类型 DAO
 *
 * @author limaofeng
 */
@Repository
public interface MessageTypeDao extends AnyJpaRepository<MessageType, String> {}
