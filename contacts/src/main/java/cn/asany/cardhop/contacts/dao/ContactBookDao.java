package cn.asany.cardhop.contacts.dao;

import cn.asany.cardhop.contacts.bean.ContactBook;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactBookDao extends JpaRepository<ContactBook, Long> {}
