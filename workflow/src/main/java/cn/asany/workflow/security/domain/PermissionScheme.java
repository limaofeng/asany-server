package cn.asany.workflow.security.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * @author penghanying @ClassName: PermissionScheme @Description: 权限方案(这里用一句话描述这个类的作用)
 * @date 2019/5/30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "IssuePermissionScheme")
@Table(name = "GD_PERMISSION_SCHEME")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class PermissionScheme extends BaseBusEntity {

  /** 主键ID */
  @Id
  @Column(name = "ID", length = 22)
  @GeneratedValue(generator = "gd_permission_scheme_gen")
  @TableGenerator(
      name = "gd_permission_scheme_gen",
      table = "sys_sequence",
      pkColumnName = "gen_name",
      pkColumnValue = "gd_permission_scheme:id",
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
