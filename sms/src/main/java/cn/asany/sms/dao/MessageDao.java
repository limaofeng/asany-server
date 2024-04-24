package cn.asany.sms.dao;

import cn.asany.sms.domain.ShortMessage;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository("sms.MessageDao")
public interface MessageDao extends AnyJpaRepository<ShortMessage, Long> {}
