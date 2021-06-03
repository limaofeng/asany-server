package cn.asany.security.oauth.bean;

import cn.asany.security.core.bean.User;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.dao.hibernate.converter.StringSetConverter;
import org.jfantasy.framework.security.oauth2.core.TokenType;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * 访问令牌
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "AUTH_ACCESS_TOKEN", uniqueConstraints = {@UniqueConstraint(columnNames = "TOKEN", name = "UN_AUTH_ACCESS_TOKEN_UNIQUE")})
public class AccessToken extends BaseBusEntity {

    @Id
    @Column(name = "ID", nullable = false, updatable = false, precision = 22)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 名称
     */
    @Column(name = "NAME", length = 50)
    private String name;
    /**
     * 凭证类型
     */
    @Column(name = "TOKEN_TYPE", length = 20)
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;
    /**
     * 范围
     */
    @Column(name = "SCOPES")
    @Convert(converter = StringSetConverter.class)
    private Set<String> scopes;
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
     * Token
     */
    @Column(name = "TOKEN", length = 500)
    private String token;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENT_ID", foreignKey = @ForeignKey(name = "FK_ACCESS_TOKEN_CLIENT"), updatable = false, nullable = false)
    private Application client;
    /**
     * 用户
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "FK_ACCESS_TOKEN_USER"), updatable = false)
    private User user;
}
