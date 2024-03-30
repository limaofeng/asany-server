package cn.asany.base.common;

import java.util.Optional;

public interface TicketTargetBuilder<T extends TicketTarget> {
  boolean supports(TicketTargetType type);

  Optional<T> build(Long id);
}
