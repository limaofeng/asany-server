package cn.asany.security;

import java.util.concurrent.CompletableFuture;

/**
 * 用户服务
 *
 * @author limaofeng
 */
public interface UserService {

  /**
   * 根据用户ID加载用户信息
   *
   * @param id 用户ID
   * @return 用户信息
   */
  CompletableFuture<User> loadUserById(Long id);
}
