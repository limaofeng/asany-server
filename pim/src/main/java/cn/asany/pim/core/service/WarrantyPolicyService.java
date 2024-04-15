package cn.asany.pim.core.service;

import cn.asany.pim.core.dao.WarrantyPolicyDao;
import cn.asany.pim.core.domain.WarrantyPolicy;
import java.util.List;
import java.util.Optional;
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

  public Optional<WarrantyPolicy> findById(Long id) {
    return this.warrantyPolicyDao.findById(id);
  }

  public WarrantyPolicy save(WarrantyPolicy warrantyPolicy) {
    return this.warrantyPolicyDao.save(warrantyPolicy);
  }

  public Optional<WarrantyPolicy> update(Long id, WarrantyPolicy warrantyPolicy, Boolean merge) {
    return this.warrantyPolicyDao
        .findById(id)
        .map(
            (result) -> {
              warrantyPolicy.setId(result.getId());
              return this.warrantyPolicyDao.update(warrantyPolicy, merge);
            });
  }

  public Integer deleteMany(PropertyFilter filter) {
    List<WarrantyPolicy> policies = this.warrantyPolicyDao.findAll(filter);
    this.warrantyPolicyDao.deleteAll(policies);
    return policies.size();
  }

  public Optional<WarrantyPolicy> delete(Long id) {
    return this.warrantyPolicyDao
        .findById(id)
        .map(
            (result) -> {
              this.warrantyPolicyDao.delete(result);
              return result;
            });
  }
}
