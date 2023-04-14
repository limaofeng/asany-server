package cn.asany.message.define.dao;

import cn.asany.message.define.domain.MessageDefinition;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 消息定义 DAO
 *
 * @author limaofeng
 */
@Repository
public interface MessageDefinitionDao extends JpaRepository<MessageDefinition, Long> {}
