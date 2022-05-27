package cn.asany.weixin.dao;

import cn.asany.weixin.domain.Fans;
import cn.asany.weixin.domain.UserKey;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FansDao extends JpaRepository<Fans, UserKey> {}
