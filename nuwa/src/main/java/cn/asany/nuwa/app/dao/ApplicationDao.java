package cn.asany.nuwa.app.dao;

import cn.asany.nuwa.app.bean.Application;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationDao extends JpaRepository<Application, Long> {

}
