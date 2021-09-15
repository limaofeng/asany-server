package cn.asany.nuwa.app.bean;

import cn.asany.nuwa.app.bean.enums.MenuType;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.dao.hibernate.converter.StringSetConverter;

/**
 * 菜单
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(
    callSuper = false,
    of = {"id", "name", "type", "path", "level", "index"})
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
  @Column(name = "SORT")
  private Integer index;
  /** 层级 */
  @Column(name = "LEVEL")
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
  private ApplicationMenu parent;
  /** 子路由 */
  @JsonInclude(content = JsonInclude.Include.NON_NULL)
  @OneToMany(
      mappedBy = "parent",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  @OrderBy("index ASC")
  private List<ApplicationMenu> children;
  /** 授权后可见 */
  @Convert(converter = StringSetConverter.class)
  @Column(name = "AUTHORITY")
  private Set<String> authority;
  /** 登录后可见 */
  @Column(name = "AUTHORIZED")
  private Boolean authorized;
  /** 是否启用 */
  @Column(name = "ENABLED")
  private Boolean enabled;
  /** 在面包屑中隐藏 */
  @Column(name = "HIDE_IN_BREADCRUMB")
  private Boolean hideInBreadcrumb;
  /** 应用 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "APPLICATION_ID",
      foreignKey = @ForeignKey(name = "FK_APPLICATION_MENU_APPID"),
      updatable = false,
      nullable = false)
  private Application application;
}
