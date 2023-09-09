package cn.asany.pm.security.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 权限方案
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "IssuePermissionScheme")
@Table(name = "PERMISSION_SCHEME")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class PermissionScheme extends BaseBusEntity {

  /** 主键ID */
  @Id
  @Column(name = "ID", length = 22)
  @GeneratedValue(generator = "permission_scheme_gen")
  @TableGenerator(
      name = "permission_scheme_gen",
      table = "sys_sequence",
      pkColumnName = "gen_name",
      pkColumnValue = "permission_scheme:id",
      valueColumnName = "gen_value")
  private Long id;

  /** 页面名称 */
  @Column(name = "NAME", length = 50)
  private String name;

  /** 页面描述 */
  @Column(name = "DESCRIPTION", length = 50)
  private String description;

  /** 权限 */
  @OneToMany(
      mappedBy = "scheme",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
  private List<GrantPermission> grants;
}
