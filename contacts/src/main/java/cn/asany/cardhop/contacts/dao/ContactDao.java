package cn.asany.cardhop.contacts.dao;

import cn.asany.cardhop.contacts.domain.Contact;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactDao extends JpaRepository<Contact, Long> {}
