package cn.asany.cardhop.contacts.dao;

import cn.asany.cardhop.contacts.bean.ContactGroup;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactGroupDao extends JpaRepository<ContactGroup, Long> {}
