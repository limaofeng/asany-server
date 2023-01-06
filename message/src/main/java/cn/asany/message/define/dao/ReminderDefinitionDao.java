package cn.asany.message.define.dao;

import cn.asany.message.define.domain.ReminderDefinition;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReminderDefinitionDao extends JpaRepository<ReminderDefinition, String> {}
