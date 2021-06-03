package org.jfantasy.framework.security.oauth2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jfantasy.framework.security.oauth2.core.TokenType;

import java.time.Instant;

/**
 * JWT 数据内容
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokenPayload {
    /**
     * 用户 ID
     */
    private Long uid;
    /**
     * 随机串
     */
    private String nonce;
    /**
     * 凭证所有人
     */
    private String name;
    /**
     * 令牌类型
     */
    @JsonProperty("token_type")
    private TokenType tokenType;
    /**
     * 客户端 ID
     */
    @JsonProperty("client_id")
    private String clientId;
    /**
     * 过期时间
     */
    @JsonProperty("expires_at")
    private Instant expiresAt;
}
