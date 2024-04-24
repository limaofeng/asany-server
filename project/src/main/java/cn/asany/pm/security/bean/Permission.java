package cn.asany.pm.security.bean;

import cn.asany.pm.security.bean.enums.PermissionsType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.Hibernate;

/**
 * 权限bean
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity(name = "IssuePermission")
@Table(name = "PERMISSION")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Permission extends BaseBusEntity {
  /** 主键ID */
  @Id
  @Column(name = "ID", length = 22)
  @TableGenerator
  private Long id;

  /** 编码 */
  @Column(name = "CODE", length = 20)
  private String code;

  /** 页面名称 */
  @Column(name = "NAME", length = 50)
  private String name;

  /** 页面描述 */
  @Column(name = "DESCRIPTION", length = 50)
  private String description;

  /** 类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 20, nullable = false)
  private PermissionsType type;

  /** 权限 */
  @OneToMany(
      mappedBy = "permission",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
  @ToString.Exclude
  private List<GrantPermission> permissions;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Permission that = (Permission) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
