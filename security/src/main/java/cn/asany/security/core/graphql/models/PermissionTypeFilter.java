package cn.asany.security.core.graphql.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;

/**
 * @author: guoyong
 * @description: 权限分类查询过滤
 * @create: 2020/6/9 15:24
 */
@Data
public class PermissionTypeFilter {
    private PropertyFilterBuilder builder = new PropertyFilterBuilder();

    @JsonProperty("name_like")
    public void setNameLike(String value) {
        if(StringUtils.isNotBlank(value)) {
            builder.contains("name", value);
        }
    }
}
