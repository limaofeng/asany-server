package cn.asany.security.auth.listener;

import org.jfantasy.framework.security.authentication.Authentication;
import org.jfantasy.framework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 登录失败
 *
 * @author limaofeng
 */
@Component
public class LoginSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

//    @Autowired
//    private EmployeeService employeeService;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        Authentication authentication = event.getAuthentication();
//        AuthenticationDetails details = (AuthenticationDetails) authentication.getDetails();
//        LoginUser user = (LoginUser) authentication.getPrincipal();
//        if (details.getLoginType() != LoginType.password) {
//            return;
//        }
//        LoginOptions options = details.getOptions();
//        if (options.getConnected()) {
//            this.employeeService.connected(user.get(DataConstant.FIELD_EMPLOYEE), options.getProvider(), options.getSnser());
//        }
    }
}