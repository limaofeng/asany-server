package cn.asany.security.core.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Id;

import lombok.*;

/**
 * 组主键
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupPrimaryKey implements Serializable {
  @Id
  @Column(name = "ID", length = 20, nullable = false)
  private String id;

  @Id
  @Column(name = "TENANT_ID", length = 24)
  private String tenantId;
}
