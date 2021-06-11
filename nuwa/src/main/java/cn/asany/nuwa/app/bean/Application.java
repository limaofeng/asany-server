package cn.asany.nuwa.app.bean;

import cn.asany.nuwa.app.bean.enums.ApplicationType;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.security.core.GrantedAuthority;
import org.jfantasy.framework.security.oauth2.core.ClientDetails;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 应用信息
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "NUWA_APPLICATION", uniqueConstraints = {@UniqueConstraint(name = "UK_APPLICATION_CLIENT_ID", columnNames = "CLIENT_ID")})
public class Application extends BaseBusEntity implements ClientDetails {
    /**
     * ID
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 应用类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", length = 20, nullable = false)
    private ApplicationType type;
    /**
     * 名称
     */
    @Column(name = "NAME")
    private String name;
    /**
     * 应用访问地址
     */
    @Column(name = "URL")
    private String url;
    /**
     * 是否启用
     */
    @Builder.Default
    @Column(name = "ENABLED")
    private Boolean enabled = true;
    /**
     * 简介
     */
    @Column(name = "DESCRIPTION")
    private String description;
    /**
     * LOGO
     */
    @Column(name = "LOGO")
    private String logo;
    /**
     * 支持的路由空间
     */
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @JoinTable(name = "NUWA_APPLICATION_ROUTESPACE",
        joinColumns = @JoinColumn(name = "APPLICATION_ID"),
        inverseJoinColumns = @JoinColumn(name = "ROUTESPACE_ID"),
        foreignKey = @ForeignKey(name = "FK_APPLICATION_ROUTESPACE_APPID")
    )
    private List<Routespace> routespaces;
    /**
     * 路由
     */
    @OneToMany(mappedBy = "application", cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<ApplicationRoute> routes;
    /**
     * 授权回调 URL
     */
    @Column(name = "CALLBACK_URL", length = 100)
    private String callbackUrl;
    /**
     * 客服端 ID
     */
    @Column(name = "CLIENT_ID", length = 20, updatable = false)
    private String clientId;
    /**
     * 客服端密钥
     */
    @OneToMany(cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @OrderBy(" createdAt desc ")
    @JoinColumn(name = "CLIENT_ID", referencedColumnName = "CLIENT_ID")
    private List<ClientSecret> clientSecretsAlias;

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
    public String getClientSecret() {
        Optional<ClientSecret> secret = getClientSecretsAlias().stream().findAny();
        if (!secret.isPresent()) {
            throw new RuntimeException("ClientSecret 未配置");
        }
        return secret.get().getSecret();
    }

    @Override
    public Set<String> getClientSecrets() {
        return getClientSecretsAlias().stream().map(ClientSecret::getSecret).collect(Collectors.toSet());
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
}
