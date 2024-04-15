package cn.asany.pim.core.service;

import cn.asany.pim.core.dao.DeviceDao;
import cn.asany.pim.core.domain.Device;
import cn.asany.pim.core.domain.WarrantyCard;
import cn.asany.pim.product.domain.Product;
import cn.asany.pim.product.service.ProductService;
import cn.asany.system.domain.ShortLink;
import cn.asany.system.service.ShortLinkService;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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

  private final ShortLinkService shortLinkService;

  @Autowired protected Environment environment;

  public DeviceService(
      DeviceDao deviceDao,
      ProductService productService,
      WarrantyCardService warrantyCardService,
      ShortLinkService shortLinkService) {
    this.deviceDao = deviceDao;
    this.productService = productService;
    this.warrantyCardService = warrantyCardService;
    this.shortLinkService = shortLinkService;
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

    ShortLink link =
        this.shortLinkService
            .findByCode(device.getQrcode())
            .orElseThrow(() -> new IllegalArgumentException("二维码不存在" + device.getQrcode()));

    device.setBrand(product.getBrand());

    List<WarrantyCard> warrantyCards = device.getWarrantyCards();
    device.setWarrantyCards(null);

    deviceDao.save(device);

    for (WarrantyCard warrantyCard : warrantyCards) {
      warrantyCard.setDevice(device);
      this.warrantyCardService.save(warrantyCard);
    }

    device.setWarrantyCards(warrantyCards);

    link.setUrl(
        environment.getProperty("MOBILE_APP_BASE_URL")
            + "/c/devices/"
            + device.getId()
            + "/support");
    link.getMetadata().put("device", device.getId().toString());
    link.setCategory("wxb:device");
    this.shortLinkService.update(link.getId(), link, false);

    return device;
  }

  @Transactional(readOnly = true)
  public Optional<Device> findById(Long id) {
    return deviceDao.findById(id);
  }

  @Transactional(readOnly = true)
  public Optional<Device> findBySN(String sn) {
    return deviceDao.findOneBy("sn", sn);
  }

  @Transactional
  public Device update(Long id, Device device, Boolean merge) {
    device.setId(id);
    return deviceDao.update(device, merge);
  }

  @Transactional
  public Optional<Device> delete(Long id) {
    Optional<Device> device = this.deviceDao.findById(id);
    device.ifPresent(
        item -> {
          item.setSn(item.getSn() + ".deleted." + System.currentTimeMillis());
          item.setNo(item.getNo() + ".deleted." + System.currentTimeMillis());
          this.deviceDao.update(item, false);
          this.shortLinkService
              .findByCode(item.getQrcode())
              .ifPresent(
                  (link) -> {
                    link.setUrl(null);
                    link.setCategory("unknown");
                    link.getMetadata().remove("device");
                  });
          this.deviceDao.delete(item);
        });
    return device;
  }

  public Optional<Device> findByNO(String id) {
    return deviceDao.findOneBy("no", id);
  }

  public Integer deleteMany(PropertyFilter filter) {
    List<Device> devices = this.deviceDao.findAll(filter);
    this.deviceDao.deleteAll(devices);
    return devices.size();
  }
}
