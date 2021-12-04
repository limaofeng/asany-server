package cn.asany.nuwa.app.bean;

import cn.asany.nuwa.app.bean.enums.MenuType;
import cn.asany.ui.resources.bean.Component;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.dao.hibernate.converter.StringSetConverter;

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
public class ApplicationMenu extends BaseBusEntity {

  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
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
}
