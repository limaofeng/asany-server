package cn.asany.shanhai.gateway.domain;

import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import net.asany.jfantasy.framework.dao.BaseBusEntity;

/**
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
@Entity
@Table(name = "SH_GATEWAY")
public class Gateway extends BaseBusEntity {

  @Id
  @Column(name = "ID")
  @TableGenerator
  private Long id;

  @Column(name = "NAME", length = 100, unique = true)
  private String name;

  @Column(name = "HOST", length = 100)
  private String host;
}
