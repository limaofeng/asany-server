package cn.asany.cardhop.integration;

import cn.asany.cardhop.contacts.domain.Contact;
import cn.asany.cardhop.contacts.domain.ContactBook;
import cn.asany.cardhop.contacts.domain.ContactGroup;
import cn.asany.cardhop.contacts.domain.enums.ContactBookType;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 通讯录
 *
 * @author limaofeng
 */
public interface IContactsService {

  /**
   * 通讯录类型
   *
   * @return 通讯录类型
   */
  ContactBookType getType();

  /**
   * 获取通讯录分组
   *
   * @param book 通讯录
   * @param namespace 命名空间
   * @return 通讯录分组
   */
  List<ContactGroup> getGroups(ContactBook book, String namespace);

  /**
   * 获取通讯录联系人
   *
   * @param book 通讯录
   * @param namespace 命名空间
   * @param pageable 分页类型
   * @param filter 过滤器
   * @return 通讯录联系人
   */
  Page<Contact> findPage(
      ContactBook book, String namespace, Pageable pageable, PropertyFilter filter);

  /**
   * 获取通讯录联系人
   *
   * @param id 联系人ID
   * @return 通讯录联系人
   */
  Optional<Contact> findContactById(Long id);
}
