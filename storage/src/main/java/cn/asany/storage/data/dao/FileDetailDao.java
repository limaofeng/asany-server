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

import cn.asany.storage.data.domain.FileDetail;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDetailDao extends AnyJpaRepository<FileDetail, Long> {

  @Modifying
  @Query(value = "delete FROM FileDetail WHERE path like :path% ")
  void deleteByPath(@Param("path") String path);

  @Modifying
  Integer clearTrash(@Param("path") String path);

  int replacePath(String path, String newPath);

  @Modifying
  Integer hideFiles(String path, boolean hidden);
}
