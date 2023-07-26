package cn.asany.security.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 用户组
 *
 * @author limaofeng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "AUTH_USERGROUP")
@GenericGenerator(name = "user_group_gen", strategy = "fantasy-sequence")
@JsonIgnoreProperties({
  "hibernate_lazy_initializer",
  "handler",
  "menus",
  "permissions",
  "roles",
  "permissions"
})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserGroup extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 用户组编码 */
  @Column(name = "CODE")
  private String code;
  /** 用户组名称 */
  @Column(name = "NAME")
  private String name;
  /** 是否启用 */
  @Column(name = "ENABLED")
  private Boolean enabled;
  /** 描述 */
  @Column(name = "DESCRIPTION")
  private String description;
}
