package cn.asany.email.data.service;

import cn.asany.email.data.bean.JamesUser;
import cn.asany.email.data.dao.UserDao;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service("JamesUserService")
public class UserService {

  private final UserDao userDao;

  public UserService(UserDao userDao) {
    this.userDao = userDao;
  }

  public boolean test(String name, String password) {
    Optional<JamesUser> optional = this.userDao.findById(name);
    return optional.map(jamesUser -> jamesUser.verifyPassword(password)).orElse(false);
  }

  public boolean contains(String name) {
    return this.userDao.existsById(name);
  }
}
