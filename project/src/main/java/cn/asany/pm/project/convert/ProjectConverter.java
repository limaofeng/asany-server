package cn.asany.pm.project.convert;

import cn.asany.pm.project.domain.Project;
import cn.asany.pm.project.graphql.input.ProjectCreateInput;
import org.mapstruct.*;

/**
 * 项目 转换器
 *
 * @author limaofeng
 */
@Mapper(
    componentModel = "spring",
    builder = @Builder(disableBuilder = true),
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ProjectConverter {

  /**
   * 将 ProjectCreateInput 转换为 Project
   *
   * @param input 路由模版
   * @return Project
   */
  @Mappings({})
  Project toProject(ProjectCreateInput input);
}
