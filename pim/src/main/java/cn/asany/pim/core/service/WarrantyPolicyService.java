package cn.asany.pim.core.service;

import cn.asany.pim.core.dao.WarrantyPolicyDao;
import cn.asany.pim.core.domain.WarrantyPolicy;
import java.util.List;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.stereotype.Service;

@Service
public class WarrantyPolicyService {

  private final WarrantyPolicyDao warrantyPolicyDao;

  public WarrantyPolicyService(WarrantyPolicyDao warrantyPolicyDao) {
    this.warrantyPolicyDao = warrantyPolicyDao;
  }

  public List<WarrantyPolicy> findAll(PropertyFilter filter) {
    return this.warrantyPolicyDao.findAll(filter);
  }
}
