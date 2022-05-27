package cn.asany.shanhai.data.dao;

import cn.asany.shanhai.data.domain.DataSetConfig;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 数据集 Dao
 *
 * @author limaofeng
 */
@Repository
public interface DataSetDao extends JpaRepository<DataSetConfig, Long> {}
