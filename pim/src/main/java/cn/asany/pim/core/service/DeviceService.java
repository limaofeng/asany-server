package cn.asany.pim.core.service;

import cn.asany.pim.core.dao.DeviceDao;
import cn.asany.pim.core.domain.Device;
import cn.asany.pim.core.domain.WarrantyCard;
import cn.asany.pim.product.domain.Product;
import cn.asany.pim.product.service.ProductService;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeviceService {
  private final DeviceDao deviceDao;

  private final ProductService productService;

  private final WarrantyCardService warrantyCardService;

  public DeviceService(
      DeviceDao deviceDao, ProductService productService, WarrantyCardService warrantyCardService) {
    this.deviceDao = deviceDao;
    this.productService = productService;
    this.warrantyCardService = warrantyCardService;
  }

  @Transactional(readOnly = true)
  public List<Device> findAll(PropertyFilter filter, int offset, int limit, Sort sort) {
    return deviceDao.findAll(filter, offset, limit, sort);
  }

  @Transactional(readOnly = true)
  public Page<Device> findPage(Pageable pageable, PropertyFilter filter) {
    return deviceDao.findPage(pageable, filter);
  }

  @Transactional
  public Device save(Device device) {
    Product product =
        this.productService
            .findById(device.getProduct().getId())
            .orElseThrow(() -> new IllegalArgumentException("产品不存在"));

    device.setBrand(product.getBrand());

    List<WarrantyCard> warrantyCards = device.getWarrantyCards();
    device.setWarrantyCards(null);

    deviceDao.save(device);

    for (WarrantyCard warrantyCard : warrantyCards) {
      warrantyCard.setDevice(device);
      this.warrantyCardService.save(warrantyCard);
    }

    device.setWarrantyCards(warrantyCards);
    return device;
  }

  @Transactional(readOnly = true)
  public Optional<Device> findById(Long id) {
    return deviceDao.findById(id);
  }

  @Transactional
  public Device update(Long id, Device device, Boolean merge) {
    device.setId(id);
    return deviceDao.update(device, merge);
  }

  @Transactional
  public Optional<Device> delete(Long id) {
    Optional<Device> device = this.deviceDao.findById(id);
    device.ifPresent(this.deviceDao::delete);
    return device;
  }
}
