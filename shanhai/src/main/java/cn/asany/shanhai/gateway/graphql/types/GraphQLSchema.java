package cn.asany.shanhai.gateway.graphql.types;

import cn.asany.shanhai.core.bean.Model;
import cn.asany.shanhai.core.bean.ModelField;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/** @author limaofeng */
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
