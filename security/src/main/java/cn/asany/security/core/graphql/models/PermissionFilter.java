package cn.asany.security.core.graphql.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;

/**
 * @author: guoyong
 * @description: 权限查询过滤
 * @create: 2020/6/9 15:24
 */
@Data
public class PermissionFilter {
  private PropertyFilterBuilder builder = new PropertyFilterBuilder();

  @JsonProperty("name_like")
  public void setNameLike(String value) {
    if (StringUtils.isNotBlank(value)) {
      builder.contains("name", value);
    }
  }

  @JsonProperty("permissionType")
  public void setPermissionType(String value) {
    if (StringUtils.isNotBlank(value)) {
      builder.equal("permissionType.id", value);
    }
  }

  @JsonProperty("id")
  public void setId(String value) {
    if (StringUtils.isNotBlank(value)) {
      builder.equal("id", value);
    }
  }
}
