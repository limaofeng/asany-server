package cn.asany.security.core.domain;

import cn.asany.security.core.domain.enums.AccessLevel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.*;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

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

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "AUTH_RESOURCE_ACTION_RESOURCE_TYPE",
      joinColumns =
          @JoinColumn(
              name = "RESOURCE_ACTION",
              foreignKey = @ForeignKey(name = "FK_RESOURCE_ACTION_RESOURCE_TYPE_A_ID")),
      inverseJoinColumns =
          @JoinColumn(
              name = "RESOURCE_TYPE",
              foreignKey = @ForeignKey(name = "FK_RESOURCE_ACTION_RESOURCE_TYPE_R_ID")))
  private List<ResourceType> resourceTypes;

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
    String leftResourceTypes =
        resourceTypes.stream().map(ResourceType::getArn).collect(Collectors.joining(","));
    String rightResourceTypes =
        action.resourceTypes.stream().map(ResourceType::getArn).collect(Collectors.joining(","));
    return leftResourceTypes.equals(rightResourceTypes);
  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + name.hashCode();
    result = 31 * result + description.hashCode();
    result = 31 * result + accessLevel.hashCode();
    result = 31 * result + resourceTypes.hashCode();
    return result;
  }
}
