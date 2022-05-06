package cn.asany.weixin.dao;

import cn.asany.weixin.bean.Fans;
import cn.asany.weixin.bean.UserKey;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FansDao extends JpaRepository<Fans, UserKey> {}
