package cn.asany.shanhai.demo.graphql.inputs;

import cn.asany.shanhai.demo.bean.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.inputs.QueryFilter;

/**
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserFilter extends QueryFilter<UserFilter, User> {

    @JsonProperty("username_contains")
    public void setUsernameContains(String value) {
        builder.contains("username", value);
    }

}
