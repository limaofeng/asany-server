package cn.asany.nuwa.app.domain;

import cn.asany.nuwa.app.domain.enums.MenuType;
import cn.asany.ui.resources.domain.Component;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import net.asany.jfantasy.framework.dao.hibernate.converter.StringSetConverter;
import net.asany.jfantasy.framework.util.common.SortNode;
import org.hibernate.Hibernate;

/**
 * 菜单
 *
 * @author limaofeng
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "NUWA_APPLICATION_MENU")
public class ApplicationMenu extends BaseBusEntity implements SortNode {

  @Id
  @Column(name = "ID")
  @TableGenerator
  private Long id;

  /** 菜单名称 */
  @Column(name = "NAME")
  private String name;

  /** 对应的图标 */
  @Column(name = "ICON")
  private String icon;

  /** 菜单类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE")
  private MenuType type;

  /** 序号 */
  @Column(name = "SORT", nullable = false)
  private Integer index;

  /** 层级 */
  @Column(name = "LEVEL", nullable = false)
  private Integer level;

  /**
   * menuType = url 时, 格式为： 打开方式： 地址 <br>
   * 1. route: 直接路由跳转 <br>
   * 2. open: 新窗口打开 <br>
   * 3. dialog: 弹出窗口打开 <br>
   * 如果未配置 打开方式，相对地址为 route， 带协议的地址，新窗口打开 <br>
   */
  @Column(name = "PATH")
  private String path;

  /** 不在菜单中显示子菜单 */
  @Column(name = "HIDE_CHILDREN_IN_MENU", nullable = false)
  private Boolean hideChildrenInMenu;

  /** 在面包屑中隐藏菜单 */
  @Column(name = "HIDE_IN_BREADCRUMB", nullable = false)
  private Boolean hideInBreadcrumb;

  /** 不在菜单中显示 */
  @Column(name = "HIDE_IN_MENU", nullable = false)
  private Boolean hideInMenu;

  /** 角标 */
  @Column(name = "BADGE")
  private String badge;

  /** 父菜单 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PID", foreignKey = @ForeignKey(name = "FK_APPLICATION_MENU_PID"))
  @ToString.Exclude
  private ApplicationMenu parent;

  /** 子路由 */
  @JsonInclude(content = JsonInclude.Include.NON_NULL)
  @OneToMany(
      mappedBy = "parent",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  @OrderBy("index ASC")
  @ToString.Exclude
  private List<ApplicationMenu> children;

  /**
   * 组件<br>
   * 可以为一级菜单设置一个展示组件
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "COMPONENT_ID",
      foreignKey = @ForeignKey(name = "FK_APPLICATION_MENU_COMPONENT"))
  @ToString.Exclude
  private Component component;

  /** 授权后可见 */
  @Convert(converter = StringSetConverter.class)
  @Column(name = "AUTHORITY")
  private Set<String> authority;

  /** 应用 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "APPLICATION_ID",
      foreignKey = @ForeignKey(name = "FK_APPLICATION_MENU_APPID"),
      updatable = false,
      nullable = false)
  @ToString.Exclude
  private Application application;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    ApplicationMenu that = (ApplicationMenu) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  @Transient
  @Override
  public Serializable getParentId() {
    return this.parent != null ? this.parent.getId() : null;
  }
}
