package cn.asany.security.auth.service;

import cn.asany.security.auth.authentication.DingtalkAuthenticationToken;
import org.jfantasy.framework.security.core.userdetails.SimpleUserDetailsService;
import org.jfantasy.framework.security.core.userdetails.UserDetails;
import org.jfantasy.framework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class DingtalkUserDetailsService
    implements SimpleUserDetailsService<DingtalkAuthenticationToken.DingtalkCredentials> {

  //    @Autowired
  //    private DingtalkService dingtalkService;
  //    @Autowired
  //    private EmployeeService employeeService;
  //    @Autowired
  //    private UserService userService;

  @Override
  public UserDetails loadUserByToken(DingtalkAuthenticationToken.DingtalkCredentials credentials)
      throws UsernameNotFoundException {
    String authCode = credentials.getAuthCode();
    Boolean connected = credentials.getConnected();
    //        DingtalkHelper dingtalk = dingtalkService.getCurrentDingtalk();
    //        DingtalkHelper.User user;
    //        try {
    //            user = dingtalk.getUserByCode(authCode);
    //        } catch (Exception e) {
    //            Map<String, Object> data = new HashMap<>();
    //            data.put("authCode", authCode);
    //            throw new DingtalkServiceException(e.getMessage(), data);
    //        }
    //
    //        Optional<Employee> optional = this.employeeService.findOneByLink(LinkType.dingtalk,
    // user.getId());
    //        if (optional.isPresent()) {
    //            return userService.buildLoginUser(optional.get());
    //        }
    //        optional = this.employeeService.findOneByMobile(user.getMobile());
    //        if (!optional.isPresent()) {
    //            throw new DingtalkUserNotFoundException("未发现钉钉对应的用户，需要引导用户使用密码完成绑定操作",
    // ObjectUtil.toMap(user));
    //        }
    //        if (!ObjectUtil.defaultValue(connected, Boolean.FALSE)) {
    //            throw new DingtalkUserNotConnectedException("已发现钉钉对应的用户，需要引导用户完成绑定操作",
    // ObjectUtil.toMap(user));
    //        }
    //        Employee employee = this.employeeService.connected(optional.get(),
    // SocialProvider.dingtalk, user.getId());
    //        return userService.buildLoginUser(employee);
    return null;
  }
}
