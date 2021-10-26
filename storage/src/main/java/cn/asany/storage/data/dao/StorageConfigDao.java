package cn.asany.storage.data.dao;

import cn.asany.storage.data.bean.StorageConfig;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 存储器
 *
 * @author limaofeng
 */
@Repository
public interface StorageConfigDao extends JpaRepository<StorageConfig, String> {}
