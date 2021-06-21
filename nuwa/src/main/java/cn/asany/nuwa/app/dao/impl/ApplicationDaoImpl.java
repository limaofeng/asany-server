package cn.asany.nuwa.app.dao.impl;

import cn.asany.nuwa.app.bean.Application;
import cn.asany.nuwa.app.dao.ApplicationDao;
import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.jfantasy.framework.dao.jpa.PropertyFilter;

import javax.persistence.EntityManager;
import java.util.Optional;

public class ApplicationDaoImpl extends ComplexJpaRepository<Application, Long> implements ApplicationDao {

    public ApplicationDaoImpl(EntityManager entityManager) {
        super(Application.class, entityManager);
    }

    @Override
    public Optional<Application> findByClientIdWithRoute(String clientId, String space) {
        return this.findOne(PropertyFilter.builder()
            .equal("clientId", clientId)
            .equal("routes.space.id", space)
            .build());
    }

    @Override
    public Optional<Application> findByIdWithRoute(Long id, String space) {
        return this.findOne(PropertyFilter.builder()
            .equal("id", id)
            .equal("routes.space.id", space)
            .build());
    }

}
