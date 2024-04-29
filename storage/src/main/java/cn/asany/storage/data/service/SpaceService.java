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
package cn.asany.storage.data.service;

import cn.asany.storage.data.dao.SpaceDao;
import cn.asany.storage.data.domain.Space;
import net.asany.jfantasy.framework.dao.hibernate.util.HibernateUtils;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import org.hibernate.Hibernate;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author limaofeng
 */
@Service
@Transactional
public class SpaceService {

  private final SpaceDao spaceDao;

  public SpaceService(SpaceDao spaceDao) {
    this.spaceDao = spaceDao;
  }

  public Page<Space> findPage(Pageable pageable, PropertyFilter filter) {
    return spaceDao.findPage(pageable, filter);
  }

  public void delete(String... ids) {
    for (String id : ids) {
      this.spaceDao.deleteById(id);
    }
  }

  @Cacheable(key = "targetClass + '#' + #p0", cacheNames = "STORAGE")
  public Space get(String id) {
    Space space = this.spaceDao.getReferenceById(id);
    return HibernateUtils.cloneEntity((Space) Hibernate.unproxy(space));
  }

  public boolean direcroryKeyUnique(String key) {
    return false; // this.directoryDao.findUniqueBy("key", key) == null;
  }
}
