package cn.asany.pim.core.service;

import cn.asany.pim.core.dao.DeviceDao;
import cn.asany.pim.core.domain.Device;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeviceService {
  private final DeviceDao deviceDao;

  public DeviceService(DeviceDao deviceDao) {
    this.deviceDao = deviceDao;
  }

  public List<Device> findAll(PropertyFilter filter, int offset, int limit, Sort sort) {
    return deviceDao.findAll(filter, offset, limit, sort);
  }

  public Page<Device> findPage(Pageable pageable, PropertyFilter filter) {
    return deviceDao.findPage(pageable, filter);
  }

  public Device save(Device device) {
    return deviceDao.save(device);
  }

  public Optional<Device> findById(Long id) {
    return deviceDao.findById(id);
  }

  public Device update(Long id, Device device, Boolean merge) {
    device.setId(id);
    return deviceDao.update(device, merge);
  }

  public Optional<Device> delete(Long id) {
    Optional<Device> device = this.deviceDao.findById(id);
    device.ifPresent(this.deviceDao::delete);
    return device;
  }
}
