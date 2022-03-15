package cn.asany.drive.graphql.resolver;

import cn.asany.base.utils.Hashids;
import cn.asany.drive.bean.CloudDrive;
import cn.asany.storage.data.bean.Space;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

@Component
public class CloudDriveGraphQLResolver implements GraphQLResolver<CloudDrive> {

  public String id(CloudDrive cloudDrive) {
    return Hashids.toId(cloudDrive.getId().toString());
  }

  public String rootFolder(CloudDrive cloudDrive) {
    Space space = cloudDrive.getSpace();
    String key = "space." + space.getId();
    return Hashids.toId(key);
  }
}
