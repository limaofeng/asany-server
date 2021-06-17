package cn.asany.nuwa.app.dao.impl;

import cn.asany.nuwa.app.bean.Routespace;
import cn.asany.nuwa.app.dao.RoutespaceDao;
import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.jfantasy.framework.dao.jpa.PropertyFilter;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Set;

public class RoutespaceDaoImpl extends ComplexJpaRepository<Routespace, String> implements RoutespaceDao {
    private  EntityManager entityManager;

    public RoutespaceDaoImpl(EntityManager entityManager) {
        super(Routespace.class, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public List<Routespace> findByIdsWithApplicationTemplateRoute(Set<String> ids) {
       return this.findAll(PropertyFilter.builder().in("id", ids).build());
    }
}
