package cn.asany.cardhop.contacts.dao;

import cn.asany.cardhop.contacts.domain.ContactGroup;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactGroupDao extends AnyJpaRepository<ContactGroup, Long> {}
