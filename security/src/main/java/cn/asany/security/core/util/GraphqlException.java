package cn.asany.security.core.util;

import org.jfantasy.framework.error.ValidationException;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2022/7/28 9:12 9:12
 */
public class GraphqlException extends ValidationException {
  public GraphqlException(Exception error) {
    super(error.getMessage());
  }

  public GraphqlException(String message) {
    super(message);
  }
}
