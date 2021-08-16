package cn.asany.security.core.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-06-12 09:18
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "AUTH_PERMISSION_SCHEME")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class PermissionScheme extends BaseBusEntity {
  /** 主键ID */
  @Id
  @Column(name = "ID", length = 22)
  @GeneratedValue(generator = "auth_permission_scheme_gen")
  @TableGenerator(
      name = "auth_permission_scheme_gen",
      table = "sys_sequence",
      pkColumnName = "gen_name",
      pkColumnValue = "auth_permission_scheme:id",
      valueColumnName = "gen_value")
  private Long id;
  /** 名称 */
  @Column(name = "NAME", length = 50)
  private String name;
  /** 描述 */
  @Column(name = "DESCRIPTION", length = 50)
  private String description;
  /** 权限 */
  @OneToMany(
      mappedBy = "scheme",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
  private List<GrantPermission> grants;
}
