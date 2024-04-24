package cn.asany.myjob.factory.dao;

import cn.asany.myjob.factory.domain.Screen;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScreenDao extends AnyJpaRepository<Screen, Long> {}
