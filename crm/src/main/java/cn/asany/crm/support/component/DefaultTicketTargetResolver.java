/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
