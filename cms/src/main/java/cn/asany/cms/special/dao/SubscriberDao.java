package cn.asany.cms.special.dao;

import cn.asany.cms.special.domain.Subscriber;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriberDao extends JpaRepository<Subscriber, Long> {}
