package cn.asany.shanhai.gateway.service;

import cn.asany.shanhai.gateway.dao.ServiceDao;
import cn.asany.shanhai.gateway.domain.Service;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.util.PinyinUtils;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.transaction.annotation.Transactional;

/** @author limaofeng */
@org.springframework.stereotype.Service
@Transactional
public class ServiceRegistryService {

  @Autowired private ServiceDao serviceDao;

  public List<Service> services() {
    return serviceDao.findAll();
  }

  public Service addService(Service service) {
    if (StringUtil.isBlank(service.getCode())) {
      service.setCode(PinyinUtils.getAll(service.getCode()));
    }
    Optional<Service> prevService =
        serviceDao.findOne(Example.of(Service.builder().code(service.getCode()).build()));
    if (!prevService.isPresent()) {
      return this.serviceDao.save(service);
    }
    service.setId(prevService.get().getId());
    return this.serviceDao.update(service, true);
  }

  public Optional<Service> getService(Long id) {
    return this.serviceDao.findById(id);
  }

  public Optional<Service> findServiceOneByCode(String code) {
    return this.serviceDao.findOne(Example.of(Service.builder().code(code).build()));
  }
}
