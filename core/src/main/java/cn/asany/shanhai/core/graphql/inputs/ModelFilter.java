package cn.asany.shanhai.core.graphql.inputs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.jfantasy.graphql.inputs.QueryFilter;

@Data
public class ModelFilter extends QueryFilter {

    @JsonProperty("username_contains")
    public void setUsernameContains(String value) {
        builder.contains("username", value);
    }

}
