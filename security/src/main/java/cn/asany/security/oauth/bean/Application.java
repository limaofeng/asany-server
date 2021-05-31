package cn.asany.security.oauth.bean;

import cn.asany.security.oauth.Ownership;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.*;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Table;
import javax.persistence.*;
import java.util.List;

/**
 * 应用
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "OAUTH_APP")
@GenericGenerator(name = "app_gen", strategy = "fantasy-sequence")
public class Application extends BaseBusEntity implements Ownership {

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

//    /**
//     * APIKEY
//     */
//    @OneToMany(mappedBy = "application", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
//    private List<ApiKey> apiKeys;

    /**
     * 是否启用
     */
    @Column(name = "ENABLED", nullable = false)
    private Boolean enabled;
    /**
     * 用户对应的角色
     */
//    @ManyToMany(targetEntity = Role.class, fetch = FetchType.LAZY)
//    @JoinTable(name = "AUTH_ROLE_APP", joinColumns = @JoinColumn(name = "APP_ID"), inverseJoinColumns = @JoinColumn(name = "ROLE_CODE"), foreignKey = @ForeignKey(name = "FK_ROLE_APP_AID"))
//    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//    private List<Role> roles;

    @Any(
        metaColumn = @Column(name = "OWNERSHIP_TYPE", length = 10, insertable = false, updatable = false),
        fetch = FetchType.LAZY
    )
    @AnyMetaDef(
        idType = "long", metaType = "string",
        metaValues = {
            @MetaValue(targetEntity = Application.class, value = "APPLICATION"),
//            @MetaValue(targetEntity = User.class, value = "PERSONAL")
        }
    )
    @JoinColumn(name = "OWNERSHIP_ID", insertable = false, updatable = false)
    private Ownership ownership;
}