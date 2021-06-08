package cn.asany.security.oauth.bean;

import cn.asany.security.oauth.Ownership;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.security.core.GrantedAuthority;
import org.jfantasy.framework.security.oauth2.core.AuthorizationGrantType;
import org.jfantasy.framework.security.oauth2.core.ClientDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.*;

/**
 * 应用
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "OAUTH_APP")
public class OAuthApplication extends BaseBusEntity implements Ownership, ClientDetails {

    @Id
    @Column(name = "ID", length = 50, updatable = false)
    private String id;
    /**
     * 名称
     */
    @Column(name = "NAME")
    private String name;
    /**
     * 描述
     */
    @Column(name = "DESCRIPTION")
    private String description;
    /**
     * 应用图标
     */
    @Column(name = "LOGO", length = 100)
    private String logo;

    /**
     * 应用地址
     */
    @Column(name = "URL", length = 100)
    private String url;
    /**
     * 授权回调 URL
     */
    @Column(name = "CALLBACK_URL", length = 100)
    private String callbackUrl;
    /**
     * 客服端 ID
     */
    @Column(name = "ID", length = 50, insertable = false, updatable = false)
    private String clientId;
    /**
     * 客服端密钥
     */
    @Column(name = "CLIENT_SECRETS", length = 50)
    private String clientSecrets;

    /**
     * 是否启用
     */
    @Column(name = "ENABLED", nullable = false)
    private Boolean enabled;

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return null;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        Set<String> grantTypes = new HashSet<>();
        grantTypes.add(AuthorizationGrantType.JWT_BEARER.getValue());
        return grantTypes;
    }

    @Override
    public String getClientSecret() {
        return this.clientSecrets;
    }

    @Override
    public Set<String> getRedirectUri() {
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