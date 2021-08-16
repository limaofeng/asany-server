package cn.asany.security.core.util;

import org.jfantasy.framework.error.ValidationException;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019/1/8 2:51 PM
 */
public class GraphqlException extends ValidationException {
  public GraphqlException(Exception error) {
    super(error.getMessage());
  }

  public GraphqlException(String message) {
    super(message);
  }
}
