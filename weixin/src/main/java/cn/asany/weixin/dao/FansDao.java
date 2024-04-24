package cn.asany.weixin.dao;

import cn.asany.weixin.domain.Fans;
import cn.asany.weixin.domain.UserKey;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FansDao extends AnyJpaRepository<Fans, UserKey> {}
