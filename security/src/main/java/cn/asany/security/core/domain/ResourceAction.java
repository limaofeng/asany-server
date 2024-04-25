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
package cn.asany.security.core.domain;

import cn.asany.security.core.domain.enums.AccessLevel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.Set;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;

/**
 * 权限操作
 *
 * @author limaofeng
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "AUTH_RESOURCE_ACTION")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ResourceAction extends BaseBusEntity {

  @Id
  @Column(name = "ID", length = 50)
  private String id;

  @Column(name = "NAME", length = 150)
  private String name;

  @Column(name = "DESCRIPTION", length = 250)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "ACCESS_LEVEL", length = 20)
  private AccessLevel accessLevel;

  /**
   * graphql 接口地址 <br>
   * 格式为: Query.users
   */
  @Column(name = "ENDPOINT", length = 50)
  private String endpoint;

  @ElementCollection
  @CollectionTable(
      name = "AUTH_RESOURCE_ACTION_RESOURCE_TYPE",
      foreignKey = @ForeignKey(name = "FK_RESOURCE_ACTION_RESOURCE_TYPE_ACTION"),
      joinColumns = @JoinColumn(name = "RESOURCE_ACTION"))
  @Column(name = "RESOURCE_TYPE_ARN", length = 250, nullable = false)
  private Set<String> resourceTypes;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ResourceAction)) {
      return false;
    }
    ResourceAction action = (ResourceAction) o;
    if (!id.equals(action.id)) {
      return false;
    }
    if (!name.equals(action.name)) {
      return false;
    }
    if (!description.equals(action.description)) {
      return false;
    }
    if (accessLevel != action.accessLevel) {
      return false;
    }
    if (!endpoint.equals(action.endpoint)) {
      return false;
    }
    String leftResourceTypes = String.join(",", resourceTypes);
    String rightResourceTypes = String.join(",", action.resourceTypes);
    return leftResourceTypes.equals(rightResourceTypes);
  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + name.hashCode();
    result = 31 * result + description.hashCode();
    result = 31 * result + accessLevel.hashCode();
    result = 31 * result + endpoint.hashCode();
    result = 31 * result + resourceTypes.hashCode();
    return result;
  }
}
