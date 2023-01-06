package cn.asany.message.define.dao;

import cn.asany.message.define.domain.MessageType;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageTypeDao extends JpaRepository<MessageType, String> {}
