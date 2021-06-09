package cn.asany.security.oauth.service;

import cn.asany.security.core.bean.User;
import cn.asany.security.oauth.bean.AccessToken;
import cn.asany.security.oauth.dao.AccessTokenDao;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.security.authentication.Authentication;
import org.jfantasy.framework.security.oauth2.JwtTokenPayload;
import org.jfantasy.framework.security.oauth2.core.AbstractTokenStore;
import org.jfantasy.framework.security.oauth2.core.OAuth2AccessToken;
import org.jfantasy.framework.security.oauth2.jwt.JwtUtils;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

/**
 * TokenStore
 *
 * @author limaofeng
 */
@Service
public class DataBaseTokenStore extends AbstractTokenStore {

    private final AccessTokenDao accessTokenDao;

    public DataBaseTokenStore(AccessTokenDao accessTokenDao) {
        this.accessTokenDao = accessTokenDao;
    }

    private Optional<AccessToken> getAccessToken(String token) {
        return this.accessTokenDao.findOne(PropertyFilter.builder().equal("token", token).build());
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken token, Authentication authentication) {
        Optional<AccessToken> optionalAccessToken = getAccessToken(token.getTokenValue());
        // 如果已经存在，更新最后使用时间及位置信息
        if (!optionalAccessToken.isPresent()) {
            JwtTokenPayload payload = JwtUtils.payload(token.getTokenValue());
            this.accessTokenDao.save(AccessToken.builder()
                .name(ObjectUtil.defaultValue(authentication.getName(), payload::getName))
                .token(token.getTokenValue())
                .tokenType(token.getTokenType())
                .issuedAt(Date.from(token.getIssuedAt()))
                .expiresAt(token.getExpiresAt() != null ? Date.from(token.getExpiresAt()) : null)
                .scopes(token.getScopes())
                .refreshToken(token.getRefreshTokenValue())
                .client(payload.getClientId())
                .lastUsedTime(Date.from(Instant.now()))
                .user(User.builder().id(payload.getUid()).build())
                .build());
        } else {
            AccessToken accessToken = optionalAccessToken.get();
            accessToken.setExpiresAt(token.getExpiresAt() != null ? Date.from(token.getExpiresAt()) : null);
            accessToken.setLastUsedTime(Date.from(Instant.now()));
        }

        super.storeAccessToken(token, authentication);
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken token) {
        Optional<AccessToken> optionalAccessToken = getAccessToken(token.getTokenValue());
        optionalAccessToken.ifPresent(this.accessTokenDao::delete);
        super.removeAccessToken(token);
    }
}
