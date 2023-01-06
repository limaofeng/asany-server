package cn.asany.message.define.dao;

import cn.asany.message.define.domain.MessageDefinition;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageDefinitionDao extends JpaRepository<MessageDefinition, String> {}
