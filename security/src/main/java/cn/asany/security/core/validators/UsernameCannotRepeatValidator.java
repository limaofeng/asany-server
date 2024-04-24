package cn.asany.security.core.validators;

import cn.asany.security.core.service.UserService;
import net.asany.jfantasy.framework.spring.validation.ValidationException;
import net.asany.jfantasy.framework.spring.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户名不能重复
 *
 * @author limaofeng
 */
@Component("user.UsernameCannotRepeatValidator")
public class UsernameCannotRepeatValidator implements Validator<String> {

  private final UserService userService;

  @Autowired
  public UsernameCannotRepeatValidator(UserService userService) {
    this.userService = userService;
  }

  @Override
  public void validate(String value) throws ValidationException {
    if (userService.findOneByUsername(value).isPresent()) {
      throw new ValidationException("用户名[" + value + "]已经存在");
    }
  }
}
