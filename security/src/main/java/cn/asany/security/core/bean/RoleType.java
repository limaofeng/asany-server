package cn.asany.security.core.bean;

import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * @author: guoyong
 * @description: 角色分类表
 * @create: 2020/6/9 11:18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "AUTH_ROLE_TYPE")
public class RoleType extends BaseBusEntity {

  private static final long serialVersionUID = 2224908963065749110L;

  /** 未知分类存值 */
  public static final String UNKNOWN = "UNKNOWN";

  /** 角色分类编码 */
  @Id
  @Column(name = "ID", length = 32)
  @OrderBy(value = "SORT ASC, NAME ASC")
  private String id;
  /** 角色分类名称 */
  @Column(name = "NAME", length = 50)
  private String name;

  /** 描述信息 */
  @Column(name = "DESCRIPTION", length = 250)
  private String description;

  /** 排序 */
  @Column(name = "SORT", length = 11)
  private int sort;

  /** 对应的角色 */
  @OneToMany(
      mappedBy = "roleType",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
  private List<Role> roles;
}
