package cn.asany.nuwa.app.domain;

import cn.asany.nuwa.app.domain.enums.ApplicationType;
import cn.asany.organization.core.domain.Organization;
import java.util.*;
import java.util.stream.Collectors;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.security.core.GrantedAuthority;
import org.jfantasy.framework.security.oauth2.core.ClientDetails;
import org.jfantasy.framework.security.oauth2.core.ClientSecretType;

/**
 * 应用信息
 *
 * @author limaofeng
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(
    name = "Graph.Application.FetchClientDetails",
    attributeNodes = {
      @NamedAttributeNode(
          value = "clientSecretsAlias",
          subgraph = "SubGraph.ClientSecret.FetchAttributes"),
    },
    subgraphs = {
      @NamedSubgraph(
          name = "SubGraph.ClientSecret.FetchAttributes",
          attributeNodes = {
            @NamedAttributeNode(value = "secret"),
            @NamedAttributeNode(value = "client")
          }),
    })
@NamedEntityGraph(
    name = "Graph.Application.FetchDetails",
    attributeNodes = {
      @NamedAttributeNode(value = "menus", subgraph = "SubGraph.ApplicationMenu.FetchComponent"),
      @NamedAttributeNode(value = "routes", subgraph = "SubGraph.ApplicationRoute.FetchComponent"),
      @NamedAttributeNode(value = "dependencies")
    },
    subgraphs = {
      @NamedSubgraph(
          name = "SubGraph.ApplicationMenu.FetchComponent",
          attributeNodes = {
            @NamedAttributeNode(value = "parent"),
            @NamedAttributeNode(value = "component")
          }),
      @NamedSubgraph(
          name = "SubGraph.ApplicationRoute.FetchComponent",
          attributeNodes = {
            @NamedAttributeNode(value = "space"),
            @NamedAttributeNode(value = "parent"),
            @NamedAttributeNode(value = "component"),
            @NamedAttributeNode(value = "breadcrumb")
          })
    })
@NamedEntityGraph(
    name = "Graph.Application.FetchRoutes",
    attributeNodes = {
      @NamedAttributeNode(value = "routes", subgraph = "SubGraph.ApplicationRoute.FetchComponent")
    },
    subgraphs = {
      @NamedSubgraph(
          name = "SubGraph.ApplicationRoute.FetchComponent",
          attributeNodes = {
            @NamedAttributeNode(value = "space"),
            @NamedAttributeNode(value = "parent"),
            @NamedAttributeNode(value = "component")
          })
    })
@NamedEntityGraph(
    name = "Graph.Application.FetchMenus",
    attributeNodes = {
      @NamedAttributeNode(value = "menus", subgraph = "SubGraph.ApplicationMenu.FetchComponent"),
    },
    subgraphs = {
      @NamedSubgraph(
          name = "SubGraph.ApplicationMenu.FetchComponent",
          attributeNodes = {
            @NamedAttributeNode(value = "parent"),
            @NamedAttributeNode(value = "component")
          }),
    })
@Entity
@Table(
    name = "NUWA_APPLICATION",
    uniqueConstraints = {
      @UniqueConstraint(name = "UK_APPLICATION_NAME", columnNames = "NAME"),
      @UniqueConstraint(name = "UK_APPLICATION_CLIENT_ID", columnNames = "CLIENT_ID")
    })
public class Application extends BaseBusEntity implements ClientDetails {
  /** ID */
  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 应用类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 20, nullable = false)
  private ApplicationType type;
  /** 名称 (全英文) */
  @Column(name = "NAME", length = 50)
  private String name;
  /** 应用访问地址 */
  @Column(name = "URL")
  private String url;
  /** 是否启用 */
  @Builder.Default
  @Column(name = "ENABLED")
  private Boolean enabled = true;
  /** 标题 */
  @Column(name = "TITLE")
  private String title;
  /** 简介 */
  @Column(name = "DESCRIPTION")
  private String description;
  /** LOGO */
  @Column(name = "LOGO")
  private String logo;
  /** 支持的路由空间 */
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "NUWA_APPLICATION_ROUTESPACE",
      joinColumns = @JoinColumn(name = "APPLICATION_ID"),
      inverseJoinColumns = @JoinColumn(name = "ROUTESPACE_ID"),
      foreignKey = @ForeignKey(name = "FK_APPLICATION_ROUTESPACE_APPID"))
  @ToString.Exclude
  private List<Routespace> routespaces;
  /** 路由 */
  @OneToMany(
      mappedBy = "application",
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
      fetch = FetchType.LAZY)
  @ToString.Exclude
  private Set<ApplicationRoute> routes;
  /** 菜单 */
  @OneToMany(
      mappedBy = "application",
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
      fetch = FetchType.LAZY)
  @ToString.Exclude
  private Set<ApplicationMenu> menus;
  /** 授权回调 URL */
  @Column(name = "CALLBACK_URL", length = 100)
  private String callbackUrl;
  /** 客服端 ID */
  @Column(name = "CLIENT_ID", length = 20, updatable = false, nullable = false)
  private String clientId;
  /** 客服端密钥 */
  @OrderBy(" createdAt desc ")
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "CLIENT_ID", referencedColumnName = "CLIENT_ID", updatable = false)
  @ToString.Exclude
  private List<ClientSecret> clientSecretsAlias;
  /** 许可证 */
  @OrderBy(" createdAt desc ")
  @OneToMany(
      mappedBy = "application",
      cascade = {CascadeType.REMOVE},
      fetch = FetchType.LAZY)
  @ToString.Exclude
  private List<Licence> licences;
  /** 所有者 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "OWNERSHIP", foreignKey = @ForeignKey(name = "FK_APPLICATION_OWNERSHIP"))
  private Organization ownership;
  /** 依赖 */
  @OrderBy(" createdAt desc ")
  @OneToMany(
      mappedBy = "application",
      cascade = {CascadeType.REMOVE},
      fetch = FetchType.LAZY)
  @ToString.Exclude
  private List<ApplicationDependency> dependencies;

  @Override
  public Map<String, Object> getAdditionalInformation() {
    return new Hashtable<>();
  }

  @Override
  public Collection<GrantedAuthority> getAuthorities() {
    return new ArrayList<>();
  }

  @Override
  public Set<String> getAuthorizedGrantTypes() {
    return new HashSet<>();
  }

  @Override
  public Set<String> getClientSecrets(ClientSecretType type) {
    return getClientSecretsAlias().stream()
        .filter(item -> item.getType() == type)
        .map(ClientSecret::getSecret)
        .collect(Collectors.toSet());
  }

  @Override
  public String getRedirectUri() {
    return null;
  }

  @Override
  public Set<String> getScope() {
    return new HashSet<>();
  }

  @Override
  public int getTokenExpires() {
    return 30;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Application that = (Application) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
