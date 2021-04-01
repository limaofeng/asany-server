package cn.asany.shanhai.demo.graphql.inputs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.jfantasy.graphql.inputs.QueryFilter;

/**
 * @author limaofeng
 */
@Data
public class UserFilter extends QueryFilter {

    @JsonProperty("username_contains")
    public void setUsernameContains(String value) {
        builder.contains("username", value);
    }

}
