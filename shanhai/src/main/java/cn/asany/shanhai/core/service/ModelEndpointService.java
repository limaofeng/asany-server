package cn.asany.shanhai.core.service;

import cn.asany.shanhai.core.dao.ModelDelegateDao;
import cn.asany.shanhai.core.dao.ModelEndpointDao;
import cn.asany.shanhai.core.domain.ModelEndpoint;
import java.util.List;
import org.hibernate.Hibernate;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ModelEndpointService {

  @Autowired private ModelEndpointDao modelEndpointDao;
  @Autowired private ModelDelegateDao modelDelegateDao;

  @Transactional(readOnly = true)
  public List<ModelEndpoint> listEndpoints(Long model) {
    List<ModelEndpoint> endpoints =
        this.modelEndpointDao.findAll(PropertyFilter.newFilter().equal("model.id", model));
    endpoints.forEach(
        item -> {
          Hibernate.initialize(item.getReturnType());
          Hibernate.initialize(item.getReturnType().getType());
          item.getArguments().forEach(arg -> Hibernate.initialize(arg.getRealType()));
          Hibernate.initialize(item.getDelegate());
        });
    return endpoints;
  }
}
