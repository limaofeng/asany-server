package cn.asany.shanhai.data.dao;

import cn.asany.shanhai.data.domain.DataSourceConfig;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 数据源 Dao
 *
 * @author limaofeng
 */
@Repository
public interface DataSourceDao extends AnyJpaRepository<DataSourceConfig, Long> {}
