package cn.asany.message.define.dao;

import cn.asany.message.define.domain.MessageType;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 消息类型 DAO
 *
 * @author limaofeng
 */
@Repository
public interface MessageTypeDao extends JpaRepository<MessageType, String> {}
