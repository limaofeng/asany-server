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
package cn.asany.drive.graphql;

import cn.asany.base.utils.Hashids;
import cn.asany.drive.domain.CloudDrive;
import cn.asany.drive.service.CloudDriveService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import net.asany.jfantasy.framework.security.LoginUser;
import net.asany.jfantasy.framework.security.SpringSecurityUtils;
import org.springframework.stereotype.Component;

@Component
public class CloudDriveGraphQLQueryAndMutationResolver
    implements GraphQLQueryResolver, GraphQLMutationResolver {

  private final CloudDriveService cloudDriveService;

  public CloudDriveGraphQLQueryAndMutationResolver(CloudDriveService cloudDriveService) {
    this.cloudDriveService = cloudDriveService;
  }

  public CloudDrive cloudDrive(String key) {
    Long id = Long.parseLong(Hashids.parseId(key));
    return this.cloudDriveService.getCloudDriveById(id);
  }

  public List<CloudDrive> cloudDrives() {
    LoginUser user = SpringSecurityUtils.getCurrentUser();
    return cloudDriveService.cloudDrives(user.getUid());
  }
}
