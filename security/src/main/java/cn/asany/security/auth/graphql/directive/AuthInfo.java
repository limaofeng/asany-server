package cn.asany.security.auth.graphql.directive;

import cn.asany.security.core.domain.PermissionCondition;
import cn.asany.security.core.domain.PermissionPolicy;
import cn.asany.security.core.domain.PermissionStatement;
import cn.asany.security.core.domain.ResourceType;
import cn.asany.security.core.domain.enums.AccessLevel;
import cn.asany.security.core.service.PermissionPolicyService;
import cn.asany.security.core.service.PermissionService;
import cn.asany.security.core.service.ResourceTypeService;
import graphql.kickstart.execution.config.GraphQLSchemaProvider;
import graphql.kickstart.tools.SchemaParser;
import graphql.language.TypeDefinition;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jfantasy.framework.dao.DataQueryContext;
import org.jfantasy.framework.dao.DataQueryContextHolder;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.authentication.Authentication;
import org.jfantasy.framework.spring.SpringBeanUtils;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.regexp.RegexpUtil;

/**
 * 权限信息
 *
 * @author limaofeng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthInfo implements Serializable {
  private String name;
  private String description;
  private AccessLevel accessLevel;
  private Set<String> resourceTypes;
  private PermissionService permissionService;
  private PermissionPolicyService permissionPolicyService;
  private ResourceTypeService resourceTypeService;

  private GraphQLSchema getGraphQLSchema() {
    if (SpringBeanUtils.containsBean(GraphQLSchema.class)) {
      return SpringBeanUtils.getBean(GraphQLSchema.class);
    }
    if (SpringBeanUtils.containsBean(GraphQLSchemaProvider.class)) {
      GraphQLSchemaProvider schemaProvider = SpringBeanUtils.getBean(GraphQLSchemaProvider.class);
      return schemaProvider.getSchema();
    }
    throw new RuntimeException("未找到 GraphQLSchema");
  }

  public void condition(Authentication authentication, Map<String, Object> args) {
    GraphQLSchema graphQLSchema = getGraphQLSchema();
    SchemaParser schemaParser = SpringBeanUtils.getBean(SchemaParser.class);
    Map<TypeDefinition<?>, Class<?>> dictionary =
        ClassUtil.getFieldValue(schemaParser, SchemaParser.class, "dictionary");
    // TODO 解析条件 (可适当缓存， 如果有与用户关联的条件，就不能缓存了。比如VALUE == 当前用户)
    // 将条件与上下文绑定
    // 应用上下文中的条件
    LoginUser userDetails = (LoginUser) authentication.getPrincipal();

    Set<String> permissions = permissionService.getPermissions(userDetails.getUid());

    List<String> paths = buildResourcePaths(args);

    Map<Class<?>, List<PropertyFilter>> rootEntityFilter = new HashMap<>();

    for (String permission : permissions) {
      PermissionPolicy policy = permissionPolicyService.loadPolicy(permission);
      List<PermissionStatement> statements =
          policy.getStatements().stream()
              .filter(this::hasOperationPermission)
              .filter(statement -> hasResourceAccess(statement, paths))
              .collect(Collectors.toList());

      Map<Class<?>, List<PropertyFilter>> entityFilter = new HashMap<>();

      for (PermissionStatement statement : statements) {
        List<PermissionCondition> conditions = statement.getCondition();
        if (conditions == null) {
          continue;
        }
        for (PermissionCondition condition : conditions) {
          ResourceType resourceType =
              resourceTypeService.findByLabel(condition.getResourceType()).orElse(null);

          if (resourceType == null) {
            continue;
          }

          GraphQLObjectType graphQLType =
              graphQLSchema.getObjectType(resourceType.getResourceName());
          Class<?> entityClass = dictionary.get(graphQLType.getDefinition());

          PropertyFilter filter = PropertyFilter.newFilter();
          filter.or(
              condition.getValues().stream()
                  .map(
                      value ->
                          condition
                              .getOperator()
                              .execute(
                                  PropertyFilter.newFilter(entityClass),
                                  condition.getFieldName(),
                                  value))
                  .toArray(PropertyFilter[]::new));

          entityFilter.computeIfAbsent(entityClass, (key) -> new ArrayList<>()).add(filter);
        }
      }

      for (Map.Entry<Class<?>, List<PropertyFilter>> entry : entityFilter.entrySet()) {
        PropertyFilter statementFilter =
            PropertyFilter.newFilter().and(entry.getValue().toArray(new PropertyFilter[0]));
        rootEntityFilter
            .computeIfAbsent(entry.getKey(), (key) -> new ArrayList<>())
            .add(statementFilter);
      }
    }

    DataQueryContext context = new DataQueryContext();

    for (Map.Entry<Class<?>, List<PropertyFilter>> rootEntry : rootEntityFilter.entrySet()) {
      PropertyFilter permissionFilter =
          PropertyFilter.newFilter().and(rootEntry.getValue().toArray(new PropertyFilter[0]));
      context.addFilter(rootEntry.getKey(), permissionFilter);
    }

    DataQueryContextHolder.setContext(context);
  }

  public boolean checkUserPermission(Authentication authentication, Map<String, Object> args) {
    LoginUser userDetails = (LoginUser) authentication.getPrincipal();

    Set<String> permissions = permissionService.getPermissions(userDetails.getUid());

    List<String> paths = buildResourcePaths(args);

    for (String permission : permissions) {
      PermissionPolicy policy = permissionPolicyService.loadPolicy(permission);
      if (policy.getStatements().stream()
          .filter(this::hasOperationPermission)
          .anyMatch(statement -> hasResourceAccess(statement, paths))) {
        return true;
      }
    }
    return false;
  }

  private boolean hasOperationPermission(PermissionStatement statement) {
    return Arrays.stream(statement.getAction())
        .anyMatch(action -> RegexpUtil.wildMatch(action, this.name));
  }

  private boolean hasResourceAccess(PermissionStatement statement, List<String> paths) {
    return paths.stream()
        .anyMatch(
            path ->
                Arrays.stream(statement.getResource())
                    .anyMatch(pattern -> RegexpUtil.wildMatch(pattern, path)));
  }

  private List<String> buildResourcePaths(Map<String, Object> args) {
    return resourceTypes.stream()
        .map(item -> replacePlaceholders(item, args))
        .collect(Collectors.toList());
  }

  public static String replacePlaceholders(String input, Map<String, Object> dataMap) {
    StringBuilder result = new StringBuilder();
    int length = input.length();
    int currentIndex = 0;

    while (currentIndex < length) {
      int placeholderStart = input.indexOf("{#", currentIndex);
      if (placeholderStart == -1) {
        // 没有找到占位符，将剩余的字符串添加到结果中
        result.append(input.substring(currentIndex));
        break;
      }

      // 将占位符之前的部分添加到结果中
      result.append(input, currentIndex, placeholderStart);

      int placeholderEnd = input.indexOf("}", placeholderStart);
      if (placeholderEnd == -1) {
        // 没有找到匹配的 '}'，将剩余的字符串添加到结果中
        result.append(input.substring(placeholderStart));
        break;
      }

      String placeholder = input.substring(placeholderStart + 2, placeholderEnd);
      String replacement = dataMap.getOrDefault(placeholder, "{#" + placeholder + "}").toString();

      // 将占位符替换为对应的数据
      result.append(replacement);

      currentIndex = placeholderEnd + 1;
    }

    return result.toString();
  }
}
