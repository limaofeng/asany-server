package cn.asany.nuwa.app.domain;

import cn.asany.nuwa.app.domain.enums.RouteType;
import cn.asany.ui.resources.domain.Component;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 路由
 *
 * @author limaofeng
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "NUWA_APPLICATION_ROUTE")
@NamedEntityGraph(
    name = "Graph.ApplicationRoute.FetchComponent",
    attributeNodes = {@NamedAttributeNode(value = "component")})
public class ApplicationRoute extends BaseBusEntity {

  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 菜单名称 */
  @Column(name = "NAME")
  private String name;
  /** 路由所属类型 PC端/M站 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "SPACE",
      foreignKey = @ForeignKey(name = "FK_APPLICATION_ROUTE_SPACE"),
      updatable = false,
      nullable = false)
  @ToString.Exclude
  private Routespace space;
  /** 路由类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", nullable = false)
  private RouteType type;
  /** 层级 */
  @Column(name = "LEVEL")
  private Integer level;
  /** 路径 */
  @Column(name = "PATH")
  private String path;
  /** 重定向 */
  @Column(name = "REDIRECT")
  private String redirect;
  /** 组件 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "COMPONENT_ID",
      foreignKey = @ForeignKey(name = "FK_APPLICATION_ROUTE_COMPONENT"))
  @ToString.Exclude
  private Component component;
  /** 对应的图标 */
  @Column(name = "ICON")
  private String icon;
  /** 父路由 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PID", foreignKey = @ForeignKey(name = "FK_APPLICATION_ROUTE_PID"))
  @ToString.Exclude
  private ApplicationRoute parent;
  /** 子路由 */
  @JsonInclude(content = JsonInclude.Include.NON_NULL)
  @OneToMany(
      mappedBy = "parent",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  @OrderBy("index ASC")
  @ToString.Exclude
  private List<ApplicationRoute> routes;
  /** 访问权限 */
  @Column(name = "access")
  private String access;
  /** 必须授权才能访问 */
  @Column(name = "AUTHORIZED")
  private Boolean authorized;
  /** 是否启用 */
  @Column(name = "ENABLED")
  private Boolean enabled;
  /** 在面包屑中隐藏菜单 */
  @Column(name = "HIDE_IN_BREADCRUMB")
  private Boolean hideInBreadcrumb;
  /** 布局设置 */
  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "pure", column = @Column(name = "LAYOUT_PURE")),
  })
  private LayoutSettings layout;
  /** 应用 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "APPLICATION_ID",
      foreignKey = @ForeignKey(name = "FK_APPLICATION_ROUTE_APPID"),
      updatable = false,
      nullable = false)
  @ToString.Exclude
  private Application application;
  /** 序号 */
  @Column(name = "SORT")
  private Integer index;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    ApplicationRoute that = (ApplicationRoute) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
