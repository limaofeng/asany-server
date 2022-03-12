package cn.asany.cardhop.contacts.service;

import cn.asany.cardhop.contacts.bean.enums.ContactBookType;
import cn.asany.cardhop.integration.IContactsService;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.map.HashedMap;

public class DefaultContactsServiceFactory {

  private final Map<ContactBookType, IContactsService> serviceMap = new HashedMap();

  public DefaultContactsServiceFactory(List<IContactsService> services) {
    for (IContactsService service : services) {
      serviceMap.put(service.getType(), service);
    }
  }

  public IContactsService getService(ContactBookType type) {
    return serviceMap.get(type);
  }
}
