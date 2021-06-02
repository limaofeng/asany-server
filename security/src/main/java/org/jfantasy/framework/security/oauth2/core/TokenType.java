package org.jfantasy.framework.security.oauth2.core;

public enum TokenType {
    /**
     * 个人 Token
     * 不能续期，但可以设置有效期的 TOKEN
     */
    PERSONAL,
    /**
     * 标准 OAUTH 的认证
     */
    TOKEN,
    /**
     * SESSION 形式的授权
     */
    SESSION
}
