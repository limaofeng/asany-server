package cn.asany.security.auth.authentication;

import cn.asany.security.auth.graphql.types.LoginOptions;
import cn.asany.security.auth.graphql.types.LoginType;
import org.jfantasy.framework.security.oauth2.OAuth2AuthenticationDetails;

/**
 * 登录详情
 *
 * @author limaofeng
 */
public class LoginAuthenticationDetails extends OAuth2AuthenticationDetails {
    private LoginType loginType;
    private LoginOptions options;

    public LoginAuthenticationDetails(String clientId, LoginType loginType, LoginOptions options) {
        super(clientId);
        this.loginType = loginType;
        this.options = options;
    }

    public LoginType getLoginType() {
        return loginType;
    }

    public LoginOptions getOptions() {
        return options;
    }
}
