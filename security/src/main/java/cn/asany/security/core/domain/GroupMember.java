package cn.asany.security.core.domain;

import javax.persistence.Column;

import lombok.Data;
import org.jfantasy.framework.dao.Tenantable;

@Data
public class GroupMember implements Tenantable {

  @Column(name = "TENANT_ID", length = 24)
  private String tenantId;
}
