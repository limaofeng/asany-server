package cn.asany.security.oauth.bean;

import cn.asany.security.oauth.Ownership;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.security.core.GrantedAuthority;
import org.jfantasy.framework.security.oauth2.core.AuthorizationGrantType;
import org.jfantasy.framework.security.oauth2.core.ClientDetails;

import javax.persistence.*;
import java.util.*;

/**
 * 应用
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "OAUTH_APP")
@GenericGenerator(name = "app_gen", strategy = "fantasy-sequence")
public class Application extends BaseBusEntity implements Ownership, ClientDetails {

    @Id
    @Column(name = "ID", updatable = false)
    @GeneratedValue(generator = "app_gen")
    private Long id;
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
    @Column(name = "CLIENT_ID", length = 20)
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