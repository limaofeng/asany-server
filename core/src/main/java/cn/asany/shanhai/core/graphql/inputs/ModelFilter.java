package cn.asany.shanhai.core.graphql.inputs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.inputs.QueryFilter;

@Data
@EqualsAndHashCode(callSuper=false)
public class ModelFilter extends QueryFilter {

    @JsonProperty("name_contains")
    public void setNameContains(String value) {
        builder.contains("name", value);
    }

}
