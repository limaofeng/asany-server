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
package cn.asany.security.core.graphql;

import cn.asany.security.core.convert.GroupConverter;
import cn.asany.security.core.domain.Group;
import cn.asany.security.core.graphql.input.GroupCreateInput;
import cn.asany.security.core.graphql.input.GroupUpdateInput;
import cn.asany.security.core.graphql.input.GroupWhereInput;
import cn.asany.security.core.graphql.types.GroupConnection;
import cn.asany.security.core.service.GroupService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.asany.jfantasy.graphql.util.Kit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * 用户组 GraphQL Root Resolver
 *
 * @author limaofeng
 */
@Component
public class GroupGraphQLRootResolver implements GraphQLMutationResolver, GraphQLQueryResolver {

  private final GroupService groupService;
  private final GroupConverter groupConverter;

  public GroupGraphQLRootResolver(
      GroupService groupService, @Autowired(required = false) GroupConverter groupConverter) {
    this.groupService = groupService;
    this.groupConverter = groupConverter;
  }

  public GroupConnection userGroups(GroupWhereInput where, int page, int pageSize, Sort orderBy) {
    Pageable pageable = PageRequest.of(page - 1, pageSize, orderBy);
    return Kit.connection(groupService.findPage(pageable, where.toFilter()), GroupConnection.class);
  }

  public Optional<Group> group(Long id) {
    return groupService.findById(id);
  }

  public Group createGroup(GroupCreateInput input) {
    return groupService.save(groupConverter.toGroup(input));
  }

  public Group updateGroup(Long id, GroupUpdateInput input, Boolean merge) {
    return groupService.update(id, groupConverter.toGroup(input));
  }

  public Boolean deleteGroup(Long id) {
    this.groupService.deleteGroup(id);
    return Boolean.TRUE;
  }

  public List<Long> addMembersToGroup(Long groupName, List<Long> userIds) {
    return new ArrayList<>(); // this.userGroupService.addGroupMembers(groupName, userIds);
  }

  public List<String> addPermissionsToGroup(String groupName, List<String> permissions) {
    return null;
  }
}
