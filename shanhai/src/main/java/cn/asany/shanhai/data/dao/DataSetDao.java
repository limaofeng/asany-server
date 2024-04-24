package cn.asany.shanhai.data.dao;

import cn.asany.shanhai.data.domain.DataSetConfig;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 数据集 Dao
 *
 * @author limaofeng
 */
@Repository
public interface DataSetDao extends AnyJpaRepository<DataSetConfig, Long> {}
