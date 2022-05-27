package cn.asany.drive.graphql;

import cn.asany.base.utils.Hashids;
import cn.asany.drive.domain.CloudDrive;
import cn.asany.drive.service.CloudDriveService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.SpringSecurityUtils;
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
