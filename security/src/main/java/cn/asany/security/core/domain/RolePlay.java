package cn.asany.security.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 业务角色
 *
 * @author limaofeng
 * @date 11:35 2021-09-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "AUTH_ROLE_PLAY")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RolePlay extends BaseBusEntity {

  @Id
  @Column(name = "ID", length = 32)
  private String id;
  /** 对应角色 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ROLE_ID", foreignKey = @ForeignKey(name = "FK_ROLE_PLAY_ROLE_ID"))
  private Role role;
  /** 业务ID */
  @Column(name = "PLAYER", length = 50)
  private String player;
}
