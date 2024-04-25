/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.nuwa.template.domain;

import cn.asany.nuwa.app.domain.enums.MenuType;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import net.asany.jfantasy.framework.dao.hibernate.converter.StringSetConverter;
import org.hibernate.Hibernate;

/**
 * 应用模版 - 菜单
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
@Table(name = "NUWA_APPLICATION_TEMPLATE_MENU")
public class ApplicationTemplateMenu extends BaseBusEntity {

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
  @Column(name = "SORT")
  private Long index;

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
  @JoinColumn(name = "PID", foreignKey = @ForeignKey(name = "FK_APPLICATION_TEMPLATE_MENU_PID"))
  @ToString.Exclude
  private ApplicationTemplateMenu parent;

  /** 子路由 */
  @JsonInclude(content = JsonInclude.Include.NON_NULL)
  @OneToMany(
      mappedBy = "parent",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  @OrderBy("index ASC")
  @ToString.Exclude
  private List<ApplicationTemplateMenu> menus;

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

  /** 应用 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "APPLICATION_ID",
      foreignKey = @ForeignKey(name = "FK_APPLICATION_TEMPLATE_MENU_APPID"),
      updatable = false,
      nullable = false)
  @ToString.Exclude
  private ApplicationTemplate application;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    ApplicationTemplateMenu that = (ApplicationTemplateMenu) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
