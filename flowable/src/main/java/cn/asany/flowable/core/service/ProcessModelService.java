package cn.asany.flowable.core.service;

import cn.asany.flowable.core.dao.ProcessModelDao;
import cn.asany.flowable.core.domain.ProcessModel;
import cn.asany.flowable.core.graphql.input.ProcessModelFilter;
import cn.asany.storage.utils.UploadUtils;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import org.flowable.ui.common.model.RemoteUser;
import org.flowable.ui.common.security.SecurityUtils;
import org.flowable.ui.modeler.model.ModelRepresentation;
import org.flowable.ui.modeler.service.FlowableModelQueryService;
import org.flowable.ui.modeler.serviceapi.ModelService;
import org.jfantasy.framework.dao.Page;
import org.jfantasy.framework.security.LoginUser;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProcessModelService {

  private final ProcessModelDao processModelDao;
  private final ModelService modelService;
  private final FlowableModelQueryService flowableModelQueryService;

  public ProcessModelService(
      ProcessModelDao processModelDao,
      ModelService modelService,
      FlowableModelQueryService flowableModelQueryService) {
    this.processModelDao = processModelDao;
    this.modelService = modelService;
    this.flowableModelQueryService = flowableModelQueryService;
  }

  public ProcessModel get(String id) {
    return this.processModelDao.getProcessModel(id);
  }

  public Page<ProcessModel> findPage(Page page, ProcessModelFilter filter) {
    return this.processModelDao.findPage(page, filter);
  }

  public ProcessModel importProcessModel(LoginUser loginUser, HttpServletRequest request, Part file)
      throws IOException {
    MultipartFile multipartFile = UploadUtils.partToMultipartFile(file);
    RemoteUser user = new RemoteUser();
    user.setId(loginUser.getUid().toString());
    SecurityUtils.assumeUser(user);
    ModelRepresentation modelRepresentation =
        this.flowableModelQueryService.importProcessModel(request, multipartFile);
    return new ProcessModel();
  }

  public void delete(String id) {
    this.modelService.deleteModel(id);
  }
}
