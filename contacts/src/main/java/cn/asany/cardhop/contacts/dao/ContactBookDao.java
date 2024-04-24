package cn.asany.cardhop.contacts.dao;

import cn.asany.cardhop.contacts.domain.ContactBook;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactBookDao extends AnyJpaRepository<ContactBook, Long> {}
