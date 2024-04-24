package cn.asany.cms.special.dao;

import cn.asany.cms.special.domain.Subscriber;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriberDao extends AnyJpaRepository<Subscriber, Long> {}
