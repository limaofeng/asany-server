package org.jfantasy.framework.security.oauth2.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.Instant;
import java.util.Set;

/**
 * OAuth2 认证详情
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OAuth2AuthenticationDetails {

    @Builder.Default
    private TokenType tokenType = TokenType.TOKEN;
    /**
     * 客户端的ID<br/>
     * 分配的 apiKey
     */
    @JsonProperty("client_id")
    private String clientId;
    /**
     * 授权模式
     */
    @JsonProperty("grant_type")
    private AuthorizationGrantType grantType;
    /**
     * 授权码<br/>
     * 授权模式 为 授权码模式 时 必填
     */
    private String code;
    /**
     * 用户名<br/>
     * 授权模式 为 密码模式 时 必填
     */
    private String username;
    /**
     * 密码<br/>
     * 授权模式 为 密码模式 时 必填
     */
    private String password;
    /**
     * 表示权限范围，可选项。
     */
    private Set<String> scopes;
    /**
     * 更新令牌<br/>
     * 授权模式 为 更新令牌模式 时 必填
     */
    @JsonProperty("refresh_token")
    private String refreshToken;
    /**
     * 过期时间
     */
    @JsonProperty("expires_at")
    private Instant expiresAt;

}
