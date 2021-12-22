package cn.asany.organization.core.graphql;

import cn.asany.organization.core.bean.TeamMember;
import cn.asany.organization.core.service.TeamMemberService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 团队成员 - GraphQL API
 *
 * @author limaofeng
 */
@Component
public class TeamMemberGraphqlApiResolver implements GraphQLQueryResolver, GraphQLMutationResolver {

  private final TeamMemberService teamMemberService;

  public TeamMemberGraphqlApiResolver(TeamMemberService teamMemberService) {
    this.teamMemberService = teamMemberService;
  }

  public List<TeamMember> teamMembers() {
    return this.teamMemberService.findAll();
  }
}
