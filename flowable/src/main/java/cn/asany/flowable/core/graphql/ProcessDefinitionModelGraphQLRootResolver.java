package cn.asany.flowable.core.graphql;

import cn.asany.storage.utils.UploadUtils;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import org.flowable.ui.common.model.RemoteUser;
import org.flowable.ui.common.model.ResultListDataRepresentation;
import org.flowable.ui.common.security.SecurityUtils;
import org.flowable.ui.modeler.model.ModelRepresentation;
import org.flowable.ui.modeler.service.FlowableModelQueryService;
import org.jfantasy.graphql.context.AuthorizationGraphQLServletContext;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ProcessDefinitionModelGraphQLRootResolver
    implements GraphQLQueryResolver, GraphQLMutationResolver {

  protected final FlowableModelQueryService flowableModelQueryService;

  public ProcessDefinitionModelGraphQLRootResolver(
      FlowableModelQueryService flowableModelQueryService) {
    this.flowableModelQueryService = flowableModelQueryService;
  }

  public int processModels(DataFetchingEnvironment environment) {
    AuthorizationGraphQLServletContext context = environment.getContext();
    HttpServletRequest request = context.getRequest();
    ResultListDataRepresentation result =
        flowableModelQueryService.getModels("", "asc", 0, request);
    return result.getSize();
  }

  public int importProcessModel(Part file, DataFetchingEnvironment environment) throws IOException {
    AuthorizationGraphQLServletContext context = environment.getContext();
    HttpServletRequest request = context.getRequest();
    MultipartFile multipartFile = UploadUtils.partToMultipartFile(file);
    RemoteUser user = new RemoteUser();
    user.setId("Test-1");
    SecurityUtils.assumeUser(user);
    ModelRepresentation model =
        this.flowableModelQueryService.importProcessModel(request, multipartFile);
    return 0;
  }
}
