package cn.asany.security.core.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * @author liumeng @Description: (这里用一句话描述这个类的作用)
 * @date 14:34 2020-04-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "AUTH_ROLE_SCOPE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RoleScope extends BaseBusEntity {
  @Id
  @Column(name = "ID", length = 50)
  private String id;

  /** 名称 */
  @Column(name = "NAME", length = 50)
  private String name;

  /** 是否启用 */
  @Column(name = "ENABLED")
  private Boolean enabled;
}
