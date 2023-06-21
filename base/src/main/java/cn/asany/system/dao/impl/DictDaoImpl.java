package cn.asany.system.dao.impl;

import cn.asany.system.dao.DictDao;
import cn.asany.system.domain.Dict;
import cn.asany.system.domain.DictKey;
import cn.asany.system.domain.DictType;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

/**
 * 字典
 *
 * @author limaofeng
 */
public class DictDaoImpl extends ComplexJpaRepository<Dict, DictKey> implements DictDao {

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
