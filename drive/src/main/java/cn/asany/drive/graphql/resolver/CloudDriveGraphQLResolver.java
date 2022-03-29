package cn.asany.drive.graphql.resolver;

import cn.asany.base.utils.Hashids;
import cn.asany.drive.bean.CloudDrive;
import cn.asany.storage.data.bean.FileDetail;
import cn.asany.storage.data.bean.Space;
import cn.asany.storage.data.service.FileService;
import cn.asany.storage.data.util.IdUtils;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CloudDriveGraphQLResolver implements GraphQLResolver<CloudDrive> {

  @Autowired private FileService fileService;

  public String id(CloudDrive cloudDrive) {
    return Hashids.toId(cloudDrive.getId().toString());
  }

  public String rootFolder(CloudDrive cloudDrive) {
    Space space = cloudDrive.getSpace();
    return IdUtils.toKey("space", space.getId());
  }

  public String recycler(CloudDrive cloudDrive) {
    Space space = cloudDrive.getSpace();
    FileDetail recycler = this.fileService.getRecycler(space.getStorage().getId(), space.getPath());
    return IdUtils.toKey("space", space.getId(), recycler.getId());
  }
}
