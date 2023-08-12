package cn.asany.security.auth.graphql.directive;

import cn.asany.security.core.domain.PermissionStatement;
import cn.asany.security.core.domain.ResourceType;
import cn.asany.security.core.domain.enums.AccessLevel;
import cn.asany.security.core.domain.enums.PermissionPolicyEffect;
import cn.asany.security.core.service.PermissionPolicyService;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jfantasy.framework.security.authentication.Authentication;
import org.jfantasy.framework.security.core.GrantedAuthority;
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
  private List<ResourceType> resourceTypes;
  private PermissionPolicyService permissionPolicyService;

  public boolean checkUserPermission(Authentication authentication, Map<String, Object> args) {
    Collection<GrantedAuthority> authorities = authentication.getAuthorities();

    List<PermissionStatement> permissionStatements =
      permissionPolicyService.loadPolicies(authorities, name);

    List<String> paths = buildResourcePaths(args);

    return permissionStatements.stream()
        .anyMatch(
            permission ->
                permission.getEffect() == PermissionPolicyEffect.Allow
                    && canAccessResource(permission.getResources(), paths));
  }

  private List<String> buildResourcePaths(Map<String, Object> args) {
    return resourceTypes.stream()
        .map(item -> replacePlaceholders(item.getArn(), args))
        .collect(Collectors.toList());
  }

  private boolean canAccessResource(String[] patterns, List<String> paths) {
    return paths.stream()
        .anyMatch(
            path ->
                Arrays.stream(patterns).anyMatch(pattern -> RegexpUtil.wildMatch(pattern, path)));
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
