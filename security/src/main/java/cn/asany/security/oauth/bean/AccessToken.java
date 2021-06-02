package cn.asany.security.oauth.bean;

import cn.asany.security.core.bean.User;
import cn.asany.security.oauth.bean.enums.TokenType;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 访问令牌
 *
 * @author limaofeng
 */
@Entity
@Table(name = "AUTH_ACCESS_TOKEN")
public class AccessToken extends BaseBusEntity {

    @Id
    @Column(name = "ID", length = 250)
    private String id;
    /**
     * 用途
     * personal 不能续期 / 可以设置有效期的 TOKEN
     * token    标准 OAUTH 的认证
     * session  SESSION 形式的授权
     */
    @Column(name = "USAGE", length = 250)
    private String usage;
    /**
     * 名称
     */
    @Column(name = "NAME", length = 50)
    private String name;
    /**
     * 凭证类型
     */
    @Column(name = "TOKEN_TYPE", length = 20)
    private TokenType tokenType;
    /**
     * 范围
     */
    @Column(name = "SCOPES")
    private String scopes;
    /**
     * 生成时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ISSUED_AT")
    private Date issuedAt;
    /**
     * 过期时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EXPIRES_AT")
    private Date expiresAt;
    /**
     * 刷新 Token
     */
    @Column(name = "REFRESH_TOKEN", length = 32)
    private String refreshToken;
    /**
     * 最后使用时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_USED_TIME")
    private Date lastUsedTime;
    /**
     * 最后的位置信息
     */
    @Column(name = "LAST_LOCATION", length = 32)
    private String lastLocation;
    /**
     * 应用
     */
    private Application application;
    /**
     * 用户
     */
    private User user;
}
