package cn.asany.base.common;

import java.util.Optional;

public interface TicketTargetResolver {

  /**
   * 解析 TicketTarget
   *
   * @param type 类型
   * @param id ID
   * @return TicketTarget
   */
  Optional<TicketTarget> resolve(TicketTargetType type, Long id);
}
