package cn.asany.security.oauth.dao;


import cn.asany.security.oauth.bean.Application;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationDao extends JpaRepository<Application, String> {
}
