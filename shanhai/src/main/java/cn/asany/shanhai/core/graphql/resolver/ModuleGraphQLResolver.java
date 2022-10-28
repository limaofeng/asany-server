package cn.asany.shanhai.core.graphql.resolver;

import cn.asany.shanhai.core.domain.Model;
import cn.asany.shanhai.core.domain.Module;
import cn.asany.shanhai.core.domain.enums.ModelType;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.List;
import java.util.stream.Collectors;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.stereotype.Component;

@Component
public class ModuleGraphQLResolver implements GraphQLResolver<Module> {

  public List<Model> models(Module module) {
    return ObjectUtil.sort(
        module.getModels().stream()
            .filter(item -> item.getType() == ModelType.ENTITY)
            .collect(Collectors.toList()),
        "createdAt",
        "asc");
  }
}
