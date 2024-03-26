package cn.asany.base.common;

import java.io.Serializable;

/**
 * 所有者接口
 *
 * @author limaofeng
 */
public interface Ownership extends Serializable {
  /**
   * ID
   *
   * @return Long
   */
  Long getId();

  /**
   * 名称
   *
   * @return String
   */
  String getName();
}
