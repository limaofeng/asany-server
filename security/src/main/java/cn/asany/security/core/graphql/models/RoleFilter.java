package cn.asany.security.core.graphql.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;

/**
 * @author: guoyong
 * @description: 角色查询过滤
 * @create: 2020/6/9 15:24
 */
@Data
public class RoleFilter {
    private PropertyFilterBuilder builder = new PropertyFilterBuilder();

    @JsonProperty("name_like")
    public void setNameLike(String value) {
        if(StringUtils.isNotBlank(value)) {
            builder.contains("name", value);
        }
    }

    @JsonProperty("organizationId")
    public void setOrganizationId(String value) {
        if(StringUtils.isNotBlank(value)) {
            builder.equal("organization.id", value);
        }
    }

    @JsonProperty("scope")
    public void setScope(String value) {
        if(StringUtils.isNotBlank(value)) {
            builder.equal("scope.id", value);
        }
    }

    @JsonProperty("roleType")
    public void setRoleType(String value) {
        if(StringUtils.isNotBlank(value)) {
            builder.equal("roleType.id", value);
        }
    }

    @JsonProperty("enabled")
    public void setEnabled(String value) {
        if(StringUtils.isNotBlank(value)) {
            //传 0 查询禁用的，其他暂时不考虑
            builder.equal("enabled", value);
        }
    }

    @JsonProperty("id")
    public void setId(String value) {
        if(StringUtils.isNotBlank(value)) {
            builder.equal("id", value);
        }
    }
}
