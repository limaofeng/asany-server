package cn.asany.security.core.graphql.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import java.util.List;

@Data
public class BusinessFilter {

    private PropertyFilterBuilder builder = new PropertyFilterBuilder();

    private String name;

    @JsonProperty(value = "id_in")
    public void setId(List<String> id) {
        builder.in("id", id);
    }
    public void setName(String name){
        builder.contains("name",name);
    }
    public List<PropertyFilter> build() {
        return this.builder.build();
    }

}
