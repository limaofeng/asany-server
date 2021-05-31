package cn.asany.security.auth.authentication;

import cn.asany.security.auth.graphql.types.LoginOptions;
import cn.asany.security.auth.graphql.types.LoginType;
import lombok.Builder;
import lombok.Data;

/**
 * 登录详情
 *
 * @author limaofeng
 */
@Builder
@Data
public class AuthenticationDetails {
    private LoginType loginType;
    private LoginOptions options;
}
