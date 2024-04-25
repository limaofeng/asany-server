/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
