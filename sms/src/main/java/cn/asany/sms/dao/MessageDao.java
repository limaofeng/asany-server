package cn.asany.sms.dao;

import cn.asany.sms.domain.ShortMessage;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("sms.MessageDao")
public interface MessageDao extends JpaRepository<ShortMessage, Long> {}
