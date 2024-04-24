package cn.asany.storage.data.dao;

import cn.asany.storage.data.domain.StorageConfig;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 存储器
 *
 * @author limaofeng
 */
@Repository
public interface StorageConfigDao extends AnyJpaRepository<StorageConfig, String> {

  /**
   * 存储用量 (单位 字节)
   *
   * @param id
   * @return
   */
  @Query(
      value = "SELECT SUM(length) FROM storage_fileobject WHERE storage_id = :id",
      nativeQuery = true)
  long usage(@Param("id") String id);

  /**
   * 文件数量
   *
   * @param id
   * @return
   */
  @Query(
      value = "SELECT COUNT(1) FROM storage_fileobject WHERE storage_id = :id",
      nativeQuery = true)
  long totalFiles(@Param("id") String id);
}
