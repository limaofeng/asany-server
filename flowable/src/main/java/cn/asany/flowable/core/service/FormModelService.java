package cn.asany.flowable.core.service;

import cn.asany.flowable.core.dao.FormModelDao;
import cn.asany.flowable.core.domain.FormModel;
import cn.asany.flowable.engine.form.converter.CustomFormJsonConverter;
import cn.asany.flowable.engine.form.model.CustomFormModel;
import cn.asany.storage.utils.UploadUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.impl.persistence.entity.DeploymentEntityImpl;
import org.flowable.form.api.FormDefinition;
import org.flowable.form.api.FormRepositoryService;
import org.flowable.form.engine.FormEngineConfiguration;
import org.flowable.form.engine.impl.persistence.entity.FormDeploymentEntityImpl;
import org.flowable.form.model.SimpleFormModel;
import org.flowable.idm.api.User;
import org.flowable.ui.common.model.RemoteUser;
import org.flowable.ui.common.security.SecurityUtils;
import org.flowable.ui.modeler.domain.Model;
import org.flowable.ui.modeler.service.FlowableFormService;
import org.flowable.ui.modeler.service.FlowableModelQueryService;
import org.flowable.ui.modeler.serviceapi.ModelService;
import org.jfantasy.framework.dao.Page;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.security.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 表单模型服务
 *
 * @author limaofeng
 */
@Service
public class FormModelService {

  @Autowired private FormModelDao formModelDao;

  @Autowired private RepositoryService repositoryService;

  @Autowired private ModelService modelService;
  @Autowired private FormRepositoryService formRepositoryService;

  @Autowired private FormEngineConfiguration formEngineConfiguration;
  private final FlowableModelQueryService flowableModelQueryService;

  private FlowableFormService flowableFormService;

  public FormModelService(FlowableModelQueryService flowableModelQueryService) {
    this.flowableModelQueryService = flowableModelQueryService;
  }

  public Page<FormModel> findPage(Page<FormModel> page, PropertyFilter filter) {
    return this.formModelDao.findPage(page, filter);
  }

  public FormModel get(String id) {
    return formModelDao.getFormModel(id);
  }

  public SimpleFormModel save(SimpleFormModel formModel, Long uid) {
    SimpleFormModel simpleFormModel = (SimpleFormModel) formModel;
    RemoteUser user = new RemoteUser();
    user.setId(String.valueOf(uid));
    Model model = new Model();
    model.setKey(simpleFormModel.getKey());
    model.setName(simpleFormModel.getName());
    model.setModelType(Model.MODEL_TYPE_FORM);
    model.setDescription(simpleFormModel.getDescription());
    model.setModelEditorJson(JSON.serialize(formModel));
    model = modelService.createModel(model, user);
    return null;
  }

  /**
   * 保存表单模型
   *
   * @param id 表单模型ID
   * @param formModel 表单模型
   * @return 表单模型
   */
  public FormModel update(String id, FormModel formModel) {
    return this.update(id, formModel, false, null);
  }

  /**
   * 保存表单模型
   *
   * @param id 表单模型ID
   * @param formModel 表单模型
   * @param newVersion 是否新版本
   * @param newVersionComment 新版本注释
   * @return 表单模型
   */
  public FormModel update(
      String id, FormModel formModel, boolean newVersion, String newVersionComment) {
    Model model = modelService.getModel(id);
    model.setName(formModel.getName());
    model.setDescription(formModel.getDescription());

    // 更新模型
    CustomFormJsonConverter jsonConverter =
        (CustomFormJsonConverter) formEngineConfiguration.getFormJsonConverter();
    CustomFormModel original = jsonConverter.convertToFormModel(model.getModelEditorJson());
    original.setKey(model.getKey());
    original.setName(model.getName());
    original.setDescription(model.getDescription());
    original.setVersion(newVersion ? model.getVersion() + 1 : model.getVersion());
    if (formModel.getComponent() != null) {
      original.setComponent(formModel.getComponent());
    }
    if (formModel.getFields() != null) {
      original.setFields(formModel.getFields());
    }
    if (formModel.getOutcomes() != null) {
      original.setOutcomes(formModel.getOutcomes());
    }
    if (formModel.getOutcomeVariableName() != null) {
      original.setOutcomeVariableName(formModel.getOutcomeVariableName());
    }
    String editorJson = jsonConverter.convertToJson(original);
    model.setModelEditorJson(editorJson);

    byte[] imageBytes = formModel.getThumbnail();
    User updatedBy = SecurityUtils.getCurrentUserObject();

    modelService.saveModel(model, editorJson, imageBytes, newVersion, newVersionComment, updatedBy);
    return this.get(id);
  }

  /**
   * 部署表单
   *
   * @param id 表单模型ID
   */
  @Transactional(rollbackFor = Exception.class)
  public void deploy(String id) {
    Model model = modelService.getModel(id);
    //    ObjectNode objectNode;
    //    objectNode = (ObjectNode) JSON.getObjectMapper().readTree(model.getModelEditorJson());
    byte[] byteStr = model.getModelEditorJson().getBytes(StandardCharsets.UTF_8);
    DeploymentEntityImpl deployment =
        (DeploymentEntityImpl)
            repositoryService
                .createDeployment()
                .name(model.getName())
                .key(model.getKey())
                //        .category(String.valueOf(objectNode.get(CATEGORY)))
                .addBytes(model.getKey() + ".form", byteStr)
                .deploy();

    FormDeploymentEntityImpl deploy =
        (FormDeploymentEntityImpl)
            formRepositoryService
                .createDeployment()
                .name(model.getName())
                .addFormBytes(model.getKey() + ".form", byteStr)
                .parentDeploymentId(deployment.getId())
                //        .category(String.valueOf(objectNode.get(CATEGORY)))
                .deploy();
    deploy.getDeployedArtifacts(FormDefinition.class).get(0).getVersion();
    //
    // updateExpandFormInfo(model.getKey(),true,deploy.getDeployedArtifacts(FormDefinition.class).get(0).getVersion());
    //      throw new GraphQLException("失败");
  }

  /**
   * 导入流程模型
   *
   * @param loginUser 当前登录用户
   * @param request 请求
   * @param file 文件
   * @return ProcessModel
   * @throws IOException IOException
   */
  public FormModel importFormModel(LoginUser loginUser, HttpServletRequest request, Part file)
      throws IOException {
    MultipartFile multipartFile = UploadUtils.partToMultipartFile(file);
    RemoteUser user = new RemoteUser();
    user.setId(loginUser.getUid().toString());
    SecurityUtils.assumeUser(user);

    repositoryService
        .createDeployment()
        .addBytes("xx.bpmn20.xml", multipartFile.getBytes())
        .deploy();
    //    ModelRepresentation modelRepresentation =
    //        this.flowableModelQueryService.importProcessModel(request, multipartFile);
    return null;
  }
}
