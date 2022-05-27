package cn.asany.security.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "AUTH_RESOURCE_TYPE")
public class ResourceType extends BaseBusEntity {
  @Id
  @Column(name = "ID", length = 50)
  private String id;
  /** 资源名称 */
  @Column(name = "NAME", length = 50)
  private String name;
}
