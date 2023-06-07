package cn.asany.flowable.core.service;

import java.util.List;
import javax.annotation.Resource;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.stereotype.Service;

/**
 * 流程定义
 *
 * @author limaofeng
 */
@Service
public class ProcessDefinitionService {

  @Resource private RepositoryService repositoryService;

  public List<ProcessDefinition> list(String processDefinitionKey, String processDefinitionName) {
    ProcessDefinitionQuery processDefinitionQuery =
        repositoryService.createProcessDefinitionQuery();
    if (StringUtil.isNotBlank(processDefinitionKey)) {
      processDefinitionQuery.processDefinitionKeyLike(processDefinitionKey);
    }
    if (StringUtil.isNotBlank(processDefinitionName)) {
      processDefinitionQuery.processDefinitionNameLike(processDefinitionName);
    }
    return processDefinitionQuery.list();
  }
}
