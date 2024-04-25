/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.nuwa.app.graphql;

import cn.asany.nuwa.YamlUtils;
import cn.asany.nuwa.app.converter.ApplicationConverter;
import cn.asany.nuwa.app.domain.Application;
import cn.asany.nuwa.app.graphql.input.ApplicationCreateInput;
import cn.asany.nuwa.app.graphql.input.ApplicationWhereInput;
import cn.asany.nuwa.app.graphql.type.ApplicationIdType;
import cn.asany.nuwa.app.service.ApplicationService;
import cn.asany.nuwa.app.service.dto.NativeApplication;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import net.asany.jfantasy.framework.util.regexp.RegexpConstant;
import net.asany.jfantasy.framework.util.regexp.RegexpUtil;
import net.asany.jfantasy.graphql.util.GraphQLUtils;
import org.springframework.stereotype.Component;

/**
 * 应用
 *
 * @author limaofeng
 */
@Slf4j
@Component
public class ApplicationGraphQLRootResolver
    implements GraphQLQueryResolver, GraphQLMutationResolver {

  private final ApplicationService applicationService;
  public final ApplicationConverter applicationConverter;

  public ApplicationGraphQLRootResolver(
      ApplicationService applicationService, ApplicationConverter applicationConverter) {
    this.applicationService = applicationService;
    this.applicationConverter = applicationConverter;
  }

  public Application importApplication(Part part) throws IOException {
    NativeApplication app = YamlUtils.load(part.getInputStream());
    if (this.applicationService.existsByClientId(app.getClientId())) {
      log.warn("实际使用过程中，应该提示 ClientId 冲突，由用户选择是否删除原来的应用");
      applicationService.deleteApplication(app.getClientId());
    }
    return applicationService.createApplication(app);
  }

  public Application createApplication(ApplicationCreateInput input) {
    NativeApplication application = applicationConverter.toNativeApplication(input);
    return applicationService.createApplication(application);
  }

  public Application updateApplication(Long id, ApplicationCreateInput input, Boolean merge) {
    return new Application();
  }

  public Boolean deleteApplication(Long id) {
    this.applicationService.deleteApplication(id);
    return Boolean.TRUE;
  }

  private static ApplicationIdType getDefaultApplicationIdType(String id) {
    return RegexpUtil.isMatch(id, RegexpConstant.VALIDATOR_INTEGE)
        ? ApplicationIdType.ID
        : ApplicationIdType.CLIENT_ID;
  }

  public Optional<Application> application(
      String id, ApplicationIdType idType, DataFetchingEnvironment environment) {
    idType = ObjectUtil.defaultValue(idType, () -> getDefaultApplicationIdType(id));
    boolean hasFetchRoutes = GraphQLUtils.hasFetchFields(environment, "routes");
    boolean hasFetchMenus = GraphQLUtils.hasFetchFields(environment, "menus");
    if (ApplicationIdType.CLIENT_ID.equals(idType)) {
      return applicationService.findDetailsByClientId(id, hasFetchRoutes, hasFetchMenus);
    }
    return applicationService.findDetailsById(Long.valueOf(id), hasFetchRoutes, hasFetchMenus);
  }

  public List<Application> applications(ApplicationWhereInput where) {
    return applicationService.findAll(where.toFilter());
  }
}
