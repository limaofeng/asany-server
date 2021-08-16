package cn.asany.security.core.service.impl; // package cn.asany.security.core.service.impl;
//
// import net.whir.hos.organization.bean.Employee;
// import cn.asany.security.core.bean.User;
// import cn.asany.security.core.service.UserService;
// import org.jfantasy.framework.security.LoginService;
// import org.jfantasy.framework.security.LoginUser;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
//
// import static net.whir.hos.organization.service.DataConstant.FIELD_EMPLOYEE;
//
/// **
// * @author limaofeng
// * @version V1.0
// * @Description: TODO
// * @date 2019-05-27 14:34
// */
// @Service
// public class UserLoginServiceImpl implements LoginService {
//
//    @Autowired
//    private UserService userService;
//
//    @Override
//    public LoginUser login(String username, String password) {
//        User user = this.userService.login(username, password);
//        if (user == null) {
//            return null;
//        }
//        return toLoginUser(user);
//    }
//
//    public LoginUser toLoginUser(User user) {
//        Employee employee = user.getEmployee();
//        LoginUser loginUser = LoginUser.builder()
//            .uid(employee != null ? employee.getId().toString() : user.getId().toString())
//            .type(user.getUserType().name())
//            .name(employee != null ?employee.getName() :user.getNickName())
//            .phone(user.get("tel"))
//            .build();
//        loginUser.set("user", user);
//        loginUser.set(FIELD_EMPLOYEE, user.getEmployee());
//        return loginUser;
//    }
//
//    public LoginUser toLoginUser(Employee employee) {
//        User user = employee.getUser();
//        LoginUser loginUser = LoginUser.builder()
//            .uid(employee.getId().toString())
//            .name(employee.getName())
//            .type(FIELD_EMPLOYEE)
//            .build();
//        if (user != null) {
//            loginUser.set("user", user);
//        }
//        loginUser.set(FIELD_EMPLOYEE, employee);
//        return loginUser;
//    }
// }
