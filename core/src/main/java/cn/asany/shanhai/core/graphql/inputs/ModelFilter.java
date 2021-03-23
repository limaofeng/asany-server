package cn.asany.shanhai.core.graphql.inputs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.jfantasy.graphql.inputs.QueryFilter;

@Data
public class ModelFilter extends QueryFilter {

    @JsonProperty("name_contains")
    public void setNameContains(String value) {
        builder.contains("name", value);
    }

}
