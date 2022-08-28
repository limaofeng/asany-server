package cn.asany.flowable;

import org.flowable.ui.modeler.serviceapi.ModelService;

public class FlowableModelService {

  private static String PROCESS_KEY_PREFIX = "pd_";

  private ModelService modelService;

  //    /**
  //     * 创建流程定义
  //     * @param modelRepresentation
  //     * @return
  //     */
  //    @Override
  //    public void createProcessModel(ModelRepresentation modelRepresentation, SaveModel
  // saveModel){
  //        String key = PROCESS_KEY_PREFIX + "_" + UUID.randomUUID().toString().replace("-","");
  //        modelRepresentation.setModelType(0);
  //        modelRepresentation.setKey(key);
  //        String json =
  // modelService.createModelJson(modelRepresentation,saveModel.getCategory(),null);
  //        Model newModel = modelService.createModel(modelRepresentation, json,
  // UserInfo.getUserId());
  //
  //        saveAndUpdateScope(saveModel.getScope(),newModel.getId(),true);
  //        ProcessModelInfo processModelInfo = saveInfo(saveModel, key, newModel.getId(),
  // ModelEnum.init);
  //        ProcessModel processModel = modelConverter.forProcessModel(newModel);
  //        processModel.setInfo(processModelInfo);
  //        return processModel;
  //    }

}
