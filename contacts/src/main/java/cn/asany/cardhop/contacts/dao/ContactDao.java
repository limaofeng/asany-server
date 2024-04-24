package cn.asany.cardhop.contacts.dao;

import cn.asany.cardhop.contacts.domain.Contact;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactDao extends AnyJpaRepository<Contact, Long> {}
