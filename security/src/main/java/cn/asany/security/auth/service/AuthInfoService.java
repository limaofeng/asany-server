package cn.asany.security.auth.service;

import cn.asany.security.auth.graphql.directive.AuthInfo;
import cn.asany.security.core.domain.AuthorizedService;
import cn.asany.security.core.domain.ResourceAction;
import cn.asany.security.core.domain.ResourceType;
import cn.asany.security.core.domain.enums.AccessLevel;
import cn.asany.security.core.service.*;
import java.util.Optional;
import java.util.Set;
import net.asany.jfantasy.framework.util.common.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 授权信息
 *
 * @author limaofeng
 */
@Service
public class AuthInfoService {
  private final ResourceActionService resourceActionService;
  private final ResourceTypeService resourceTypeService;
  private final AuthorizedServiceService authorizedServiceService;
  private final PermissionService permissionService;
  private final PermissionPolicyService permissionPolicyService;

  public AuthInfoService(
      PermissionService permissionService,
      ResourceActionService resourceActionService,
      ResourceTypeService resourceTypeService,
      PermissionPolicyService permissionPolicyService,
      AuthorizedServiceService authorizedServiceService) {
    this.permissionService = permissionService;
    this.resourceActionService = resourceActionService;
    this.resourceTypeService = resourceTypeService;
    this.permissionPolicyService = permissionPolicyService;
    this.authorizedServiceService = authorizedServiceService;
  }

  @Transactional(rollbackFor = RuntimeException.class)
  public AuthInfo save(
      String name,
      String description,
      String endpoint,
      AccessLevel accessLevel,
      Set<String> resourceTypes) {

    this.resourceTypeSaveOrUpdate(resourceTypes);

    Optional<ResourceAction> optional = this.resourceActionService.findById(name);

    ResourceAction action =
        ResourceAction.builder()
            .id(name)
            .name(name)
            .endpoint(endpoint)
            .description(description)
            .accessLevel(accessLevel)
            .resourceTypes(resourceTypes)
            .build();

    if (!optional.isPresent() || !action.equals(optional.get())) {
      action = this.resourceActionService.save(action);
    }

    return AuthInfo.builder()
        .name(action.getName())
        .description(action.getDescription())
        .accessLevel(action.getAccessLevel())
        .resourceTypes(action.getResourceTypes())
        .permissionService(permissionService)
        .permissionPolicyService(permissionPolicyService)
        .resourceTypeService(resourceTypeService)
        .build();
  }

  private void resourceTypeSaveOrUpdate(Set<String> resourceTypeStrings) {
    for (String arn : resourceTypeStrings) {
      String label = arn.substring(0, arn.indexOf("/"));
      ResourceType resourceType =
          this.resourceTypeService
              .findByLabel(label)
              .orElseGet(
                  () -> {
                    String[] values = arn.split(":");
                    String resourceName =
                        StringUtil.upperCaseFirst(values[values.length - 1].split("/")[0]);
                    AuthorizedService service = this.getServiceOrSave(values[0]);
                    return this.resourceTypeService.save(
                        ResourceType.builder()
                            .label(label)
                            .resourceName(resourceName)
                            .service(service)
                            .arn(arn)
                            .build());
                  });
      if (!resourceType.getArns().contains(arn)) {
        resourceType.getArns().add(arn);
        this.resourceTypeService.update(resourceType);
      }
    }
  }

  private AuthorizedService getServiceOrSave(String id) {
    return this.authorizedServiceService
        .findById(id)
        .orElseGet(
            () -> this.authorizedServiceService.save(AuthorizedService.builder().id(id).build()));
  }
}
