package cn.asany.shanhai.data.dao;

import cn.asany.shanhai.data.domain.DataSourceConfig;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 数据源 Dao
 *
 * @author limaofeng
 */
@Repository
public interface DataSourceDao extends JpaRepository<DataSourceConfig, Long> {}
