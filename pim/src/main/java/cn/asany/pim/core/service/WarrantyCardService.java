package cn.asany.pim.core.service;

import cn.asany.pim.core.dao.WarrantyCardDao;
import cn.asany.pim.core.domain.WarrantyCard;
import org.springframework.stereotype.Service;

@Service
public class WarrantyCardService {

  private final WarrantyCardDao warrantyCardDao;

  public WarrantyCardService(WarrantyCardDao warrantyCardDao) {
    this.warrantyCardDao = warrantyCardDao;
  }

  public void save(WarrantyCard warrantyCard) {
    this.warrantyCardDao.save(warrantyCard);
  }
}
