package cn.asany.workflow.state.graphql;

import cn.asany.workflow.state.service.StateService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Component
public class StateGraphQLMutationResolver implements GraphQLMutationResolver {


    @Autowired
    private StateService issueStatusService;

     /**
     * 增加任务状态
     */
   /*protected IssueStatus createIssueStatus(IssueStatus issueStatus) {
        return issueStatusService.save(issueStatus);
    }*/


}
