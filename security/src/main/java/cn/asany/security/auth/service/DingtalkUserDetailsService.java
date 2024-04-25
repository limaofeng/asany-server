/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.security.auth.service;

import cn.asany.security.auth.authentication.DingtalkAuthenticationToken;
import net.asany.jfantasy.framework.security.core.userdetails.SimpleUserDetailsService;
import net.asany.jfantasy.framework.security.core.userdetails.UserDetails;
import net.asany.jfantasy.framework.security.core.userdetails.UsernameNotFoundException;
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
