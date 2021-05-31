package cn.asany.security.auth.graphql;

import cn.asany.security.auth.authentication.*;
import cn.asany.security.auth.graphql.types.LoginOptions;
import cn.asany.security.auth.graphql.types.LoginType;
import cn.asany.security.oauth.service.AccessTokenService;
import graphql.GraphQLError;
import graphql.kickstart.spring.error.ErrorContext;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.security.AuthenticationException;
import org.jfantasy.framework.security.AuthenticationManager;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.authentication.AbstractAuthenticationToken;
import org.jfantasy.framework.security.authentication.Authentication;
import org.jfantasy.framework.security.authentication.BadCredentialsException;
import org.jfantasy.framework.security.authentication.UsernamePasswordAuthenticationToken;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.graphql.util.GraphQLErrorUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 登录服务
 *
 * @author limaofeng
 * @version V1.0
 * @date 2019-04-01 15:17
 */
@Component
@Slf4j
public class LoginGraphQLMutationResolver implements GraphQLMutationResolver {

    private final AuthenticationManager authenticationManager;

    private final AccessTokenService accessTokenService;

    public LoginGraphQLMutationResolver(AuthenticationManager authenticationManager,AccessTokenService accessTokenService) {
        this.authenticationManager = authenticationManager;
        this.accessTokenService = accessTokenService;
    }

    @ExceptionHandler(value = AuthenticationException.class)
    public GraphQLError loginExceptionHandler(AuthenticationException e, ErrorContext context) {
        return GraphQLErrorUtils.buildGraphQLError(context, e);
    }

    public LoginUser login(LoginType loginType, String username, String password, String authCode, String tmpAuthCode, String singleCode, LoginOptions options) {
        Authentication token = buildAuthenticationToken(loginType, username, password, authCode, tmpAuthCode, singleCode, options);

        Authentication authentication = authenticationManager.authenticate(token);

        if (!authentication.isAuthenticated()) {
            throw new BadCredentialsException("Bad credentials");
        }

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String clientId = "";
        loginUser.setToken(accessTokenService.implicit(clientId, loginUser));
        return loginUser;
    }

    private Authentication buildAuthenticationToken(LoginType loginType, String username, String password, String authCode, String tmpAuthCode, String singleCode, LoginOptions options) {
        options = ObjectUtil.defaultValue(options, LoginOptions.builder().build());
        AuthenticationDetails details = AuthenticationDetails.builder().loginType(loginType).options(options).build();
        AbstractAuthenticationToken authentication;

        switch (loginType) {
            case password:
                authentication = new UsernamePasswordAuthenticationToken(username, password);
                break;
            case dingtalk:
                authentication = new DingtalkAuthenticationToken(DingtalkAuthenticationToken.DingtalkCredentials.builder().authCode(authCode).connected(options.getConnected()).build());
                break;
            case WeChatCP:
                authentication = new WeChatAuthenticationToken(WeChatAuthenticationToken.WeChatCredentials.builder()/*.type(Oauth2.MobileType.WeChatCP)*/.authCode(authCode).connected(options.getConnected()).build());
                break;
            case WeChatPM:
                authentication = new WeChatAuthenticationToken(WeChatAuthenticationToken.WeChatCredentials.builder()/*.type(Oauth2.MobileType.WeChatPM)*/.authCode(authCode).connected(options.getConnected()).build());
                break;
            case tourist:
                authentication = new AnonymousAuthenticationToken();
                break;
            case single:
                authentication = new TemporaryAuthenticationToken(singleCode);
                break;
            default:
                throw new IllegalStateException("Unexpected AuthenticationToken By: " + loginType);
        }

        authentication.setDetails(details);

        return authentication;
    }

}

//        options = ObjectUtil.defaultValue(options, LoginOptions.builder().build());
//        switch (loginType) {
//            case dingtalk:
//                return loginByDingtalk(authCode, options);
//            case WeChatCP:
//                return mobileLogin(Oauth2.MobileType.WeChatCP, authCode);
//            case WeChatPM:
//                return mobileLogin(Oauth2.MobileType.WeChatPM, authCode);
//            case single:
//                Employee model = employeeService.findOneBySN(singleCode);
//                if (model == null) {
//                    return null;
//                }
//                return loginService.toLoginUser(model);
//            case tourist:
//                return touristLogin();
//            default:
//                LoginUser user = loginService.login(username, password);
//                if (StringUtil.isNotBlank(options)) {
//                    syncEmployeeDao.save(SyncEmployee.builder().mobile(user.getPhone())
//                        .mobileId(options.getSnser())
//                        .locId(user.getUid())
//                        .status(SyncStatus.over)
//                        .baseId(options.getPlatform()).build());
//                }
//                if ("ezoffice".equals(user.getType())) {
//                    Optional<Employee> optional = employeeService.getByLink(LinkType.ezoffice, user.getUid());
//                    if (optional.isPresent()) {
//                        return loginService.toLoginUser(optional.get());
//                    }
//                    return null;
//                }
//                if (options.getConnected()) {
//                    this.employeeService.connected(user.get(DataConstant.FIELD_EMPLOYEE), options.getProvider(), options.getSnser());
//                }
//                return user;
//        }




