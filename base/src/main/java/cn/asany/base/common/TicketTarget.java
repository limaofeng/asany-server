package cn.asany.base.common;

import java.io.Serializable;

public interface TicketTarget extends Serializable {
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
