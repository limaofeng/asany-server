package cn.asany.shanhai.core.graphql;

import cn.asany.shanhai.core.convert.ModuleConverter;
import cn.asany.shanhai.core.domain.Module;
import cn.asany.shanhai.core.graphql.inputs.ModuleCreateInput;
import cn.asany.shanhai.core.graphql.inputs.ModuleFilter;
import cn.asany.shanhai.core.graphql.inputs.ModuleUpdateInput;
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

  private final ModuleConverter moduleConverter;

  public ModuleGraphQLRootResolver(ModuleService moduleService, ModuleConverter moduleConverter) {
    this.moduleService = moduleService;
    this.moduleConverter = moduleConverter;
  }

  public List<Module> modules(ModuleFilter filter, int offset, int limit, Sort orderBy) {
    return moduleService.findAll(filter.build(), offset, limit, orderBy);
  }

  public ModuleConnection modulesConnection(
      ModuleFilter filter, int page, int pageSize, Sort orderBy) {
    Pageable pageable = PageRequest.of(page - 1, pageSize, orderBy);
    return Kit.connection(moduleService.findPage(pageable, filter.build()), ModuleConnection.class);
  }

  public Optional<Module> module(Long id) {
    return moduleService.findById(id);
  }

  public Module createModule(ModuleCreateInput input) {
    return moduleService.save(moduleConverter.toModule(input));
  }

  public Module updateModule(Long id, Boolean merge, ModuleUpdateInput input) {
    return moduleService.update(id, moduleConverter.toModule(input), merge);
  }

  public Boolean deleteModule(Long id) {
    this.moduleService.delete(id);
    return Boolean.TRUE;
  }

  public int deleteManyModules(Long[] ids) {
    return this.moduleService.delete(ids);
  }
}
