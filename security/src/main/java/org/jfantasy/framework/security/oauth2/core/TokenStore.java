package org.jfantasy.framework.security.oauth2.core;

import org.jfantasy.framework.security.authentication.Authentication;
import org.jfantasy.framework.security.oauth2.server.BearerTokenAuthenticationToken;
import org.jfantasy.framework.security.oauth2.server.authentication.BearerTokenAuthentication;

import java.util.Collection;

/**
 * ToKen 存储器
 *
 * @author limaofeng
 */
public interface TokenStore {

    /**
     * 通过 Token 获取身份
     *
     * @param token
     * @return
     */
    BearerTokenAuthentication readAuthentication(BearerTokenAuthenticationToken token);

    /**
     * 通过 Token 获取身份
     *
     * @param token
     * @return
     */
    BearerTokenAuthentication readAuthentication(String token);

    /**
     * 存储 Token 对应的获取身份
     *
     * @param token
     * @return
     */
    void storeAccessToken(OAuth2AccessToken token, Authentication authentication);

    /**
     * 获取 AccessToken 信息
     *
     * @param tokenValue
     * @return
     */
    OAuth2AccessToken readAccessToken(String tokenValue);

    /**
     * 移除 AccessToken 信息
     *
     * @param token
     */
    void removeAccessToken(OAuth2AccessToken token);

    /**
     * 存储 refreshToken 对应的获取身份
     *
     * @param refreshToken
     * @param authentication
     */
    void storeRefreshToken(OAuth2RefreshToken refreshToken, Authentication authentication);

    /**
     * 获取 readRefreshToken 信息
     *
     * @param tokenValue
     * @return
     */
    OAuth2RefreshToken readRefreshToken(String tokenValue);

    /**
     * 通过 RefreshToken 获取身份信息
     *
     * @param token
     * @return
     */
    BearerTokenAuthentication readAuthenticationForRefreshToken(OAuth2RefreshToken token);

    /**
     * 移除 refreshToken
     *
     * @param token
     */
    void removeRefreshToken(OAuth2RefreshToken token);

    /**
     * 通过 refreshToken 移除 AccessToken
     *
     * @param refreshToken
     */
    void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken);

    /**
     * 获取 OAuth2AccessToken
     *
     * @param authentication
     * @return
     */
    OAuth2AccessToken getAccessToken(BearerTokenAuthentication authentication);

    /**
     * 获取 clientId 的全部 Token
     *
     * @param clientId
     * @param userName
     * @return
     */
    Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName);

    /**
     * 获取 clientId 的全部 Token
     *
     * @param clientId
     * @return
     */
    Collection<OAuth2AccessToken> findTokensByClientId(String clientId);

}
