package cn.asany.nuwa.app.dao;

import cn.asany.nuwa.app.domain.Licence;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 许可证 Dao
 *
 * @author limaofeng
 */
@Repository
public interface LicenceDao extends JpaRepository<Licence, Long> {}
