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
package cn.asany.system.dao.impl;

import cn.asany.system.dao.DictDao;
import cn.asany.system.domain.Dict;
import cn.asany.system.domain.DictKey;
import cn.asany.system.domain.DictType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;
import java.util.stream.Collectors;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.dao.jpa.SimpleAnyJpaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

/**
 * 字典
 *
 * @author limaofeng
 */
public class DictDaoImpl extends SimpleAnyJpaRepository<Dict, DictKey> implements DictDao {

  public DictDaoImpl(EntityManager entityManager) {
    super(Dict.class, entityManager);
  }

  @Override
  public int deleteDictByType(String type) {
    DictType dictType = em.getReference(DictType.class, type);

    Specification spec =
        toSpecification(PropertyFilter.newFilter().startsWith("path", dictType.getPath()));

    Class clazz = DictType.class;
    List<DictType> types = super.getQuery(spec, clazz, Sort.unsorted()).getResultList();

    List<String> ids = types.stream().map(DictType::getId).collect(Collectors.toList());

    String sql = "UPDATE SYS_DICT SET PTYPE = NULL, PCODE = NULL WHERE PTYPE IN (:ids)";
    Query query = em.createNativeQuery(sql);
    query.setParameter("ids", ids);
    query.executeUpdate();

    sql = "DELETE FROM SYS_DICT WHERE TYPE IN (:ids)";
    query = em.createNativeQuery(sql);
    query.setParameter("ids", ids);
    return query.executeUpdate();
  }
}
