package cn.asany.nuwa.app.dao;

import cn.asany.nuwa.app.domain.Licence;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 许可证 Dao
 *
 * @author limaofeng
 */
@Repository
public interface LicenceDao extends AnyJpaRepository<Licence, Long> {}
