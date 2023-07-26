package cn.asany.cardhop.contacts.service;

import cn.asany.cardhop.contacts.domain.enums.ContactBookType;
import cn.asany.cardhop.integration.IContactsService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 默认通讯录服务工厂
 *
 * @author limaofeng
 */
public class DefaultContactsServiceFactory {

  private final Map<ContactBookType, IContactsService> serviceMap = new HashMap<>();

  public DefaultContactsServiceFactory(List<IContactsService> services) {
    for (IContactsService service : services) {
      serviceMap.put(service.getType(), service);
    }
  }

  public IContactsService getService(ContactBookType type) {
    return serviceMap.get(type);
  }
}
