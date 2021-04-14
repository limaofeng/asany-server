package cn.asany.shanhai.gateway.util;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.beans.Transient;

@Data
@Builder
@EqualsAndHashCode(of = {"id", "description", "type"})
public class GraphQLFieldArgument {
    private String id;
    private String description;
    private String type;
    private GraphQLSchema schema;

    @Transient
    public GraphQLSchema getSchema() {
        return schema;
    }

    @Transient
    public GraphQLObjectType getTypeDefinition() {
        return this.schema.getType(this.type);
    }

}
