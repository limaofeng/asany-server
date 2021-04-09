package cn.asany.shanhai.schema.bean;

import graphql.language.Type;
import graphql.language.TypeName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GraphQLFieldDefinition {
    private String id;
    private String description;
    private String type;

    public static class GraphQLFieldDefinitionBuilder {

        public GraphQLFieldDefinitionBuilder type(Type type) {
            if(type instanceof TypeName){
                this.type = ((TypeName) type).getName();
            }
            System.out.println(type);
            return this;
        }

    }

}
