package cn.asany.shanhai.core.graphql;

import cn.asany.shanhai.core.domain.Module;
import cn.asany.shanhai.core.graphql.inputs.ModuleFilter;
import cn.asany.shanhai.core.graphql.types.ModuleConnection;
import cn.asany.shanhai.core.service.ModuleService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import java.util.Optional;
import org.jfantasy.graphql.util.Kit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class ModuleGraphQLRootResolver implements GraphQLMutationResolver, GraphQLQueryResolver {

  private final ModuleService moduleService;

  public ModuleGraphQLRootResolver(ModuleService moduleService) {
    this.moduleService = moduleService;
  }

  public List<Module> modules(ModuleFilter filter, int first, int offset, Sort orderBy) {
    Pageable pageable = PageRequest.of(offset, first, orderBy);
    return moduleService.findPage(pageable, filter.build()).getContent();
  }

  public ModuleConnection modulesConnection(
      ModuleFilter filter, int page, int pageSize, Sort orderBy) {
    Pageable pageable = PageRequest.of(page - 1, pageSize, orderBy);
    return Kit.connection(moduleService.findPage(pageable, filter.build()), ModuleConnection.class);
  }

  public Optional<Module> module(Long id) {
    return moduleService.findById(id);
  }
}
