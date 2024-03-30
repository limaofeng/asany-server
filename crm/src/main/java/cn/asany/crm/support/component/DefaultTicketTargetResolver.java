package cn.asany.crm.support.component;

import cn.asany.base.common.TicketTarget;
import cn.asany.base.common.TicketTargetBuilder;
import cn.asany.base.common.TicketTargetResolver;
import cn.asany.base.common.TicketTargetType;
import java.util.List;
import java.util.Optional;

public class DefaultTicketTargetResolver implements TicketTargetResolver {

  private final List<TicketTargetBuilder<? extends TicketTarget>> builders;

  public DefaultTicketTargetResolver(List<TicketTargetBuilder<? extends TicketTarget>> builders) {
    this.builders = builders;
  }

  public Optional<TicketTarget> resolve(TicketTargetType type, Long id) {
    for (TicketTargetBuilder<? extends TicketTarget> builder : builders) {
      if (builder.supports(type)) {
        return (Optional<TicketTarget>) builder.build(id);
      }
    }
    return Optional.empty();
  }
}
