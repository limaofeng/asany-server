package org.jfantasy.framework.security.oauth2.core;

import org.jfantasy.framework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface ClientDetails {

    /**
     * 附加信息
     *
     * @return
     */
    Map<String, Object> getAdditionalInformation();

    /**
     * 权限
     *
     * @return
     */
    Collection<GrantedAuthority> getAuthorities();

    /**
     * 支持的授权方式
     *
     * @return
     */
    Set<String> getAuthorizedGrantTypes();

    /**
     * 客户端 ID
     *
     * @return
     */
    String getClientId();

    /**
     * 客户端密钥
     *
     * @return
     */
    String getClientSecret();

    /**
     * 跳转地址
     *
     * @return
     */
    Set<String> getRedirectUri();

    /**
     * 授权范围
     *
     * @return
     */
    Set<String> getScope();

    /**
     * Token 失效时间（分钟）
     *
     * @return
     */
    int getTokenExpires();
}
