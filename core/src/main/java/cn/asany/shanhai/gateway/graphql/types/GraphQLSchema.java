package cn.asany.shanhai.gateway.graphql.types;

import cn.asany.shanhai.core.bean.Model;
import cn.asany.shanhai.core.bean.ModelField;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author limaofeng
 */
@Data
@Builder
public class GraphQLSchema {
    private String id;
    private String name;
    private List<ModelField> endpoints;
    private List<ModelField> queries;
    private List<ModelField> mutations;
    private List<Model> types;
}
