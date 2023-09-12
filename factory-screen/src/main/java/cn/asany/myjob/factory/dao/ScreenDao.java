package cn.asany.myjob.factory.dao;

import cn.asany.myjob.factory.domain.Screen;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScreenDao extends JpaRepository<Screen, Long> {}
