package cn.asany.pim.core.dao;

import cn.asany.pim.core.domain.Device;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 设备数据访问对象
 *
 * @author limaofeng
 */
@Repository("pim.deviceDao")
public interface DeviceDao extends JpaRepository<Device, Long> {}
