package cn.asany.email.data.dao;

import cn.asany.email.data.bean.JamesUser;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("JamesUserDao")
public interface UserDao extends JpaRepository<JamesUser, String> {}
