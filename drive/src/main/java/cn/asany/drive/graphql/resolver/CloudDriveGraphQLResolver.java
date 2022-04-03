package cn.asany.drive.graphql.resolver;

import cn.asany.base.utils.Hashids;
import cn.asany.drive.bean.CloudDrive;
import cn.asany.storage.data.bean.FileDetail;
import cn.asany.storage.data.bean.Space;
import cn.asany.storage.data.service.FileService;
import cn.asany.storage.data.util.IdUtils;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

@Component
public class CloudDriveGraphQLResolver implements GraphQLResolver<CloudDrive> {

  private final FileService fileService;

  public CloudDriveGraphQLResolver(FileService fileService) {
    this.fileService = fileService;
  }

  public String id(CloudDrive cloudDrive) {
    return Hashids.toId(cloudDrive.getId().toString());
  }

  public String rootFolder(CloudDrive cloudDrive) {
    Space space = cloudDrive.getSpace();
    return IdUtils.toKey("space", space.getId());
  }

  public String recycleBin(CloudDrive cloudDrive) {
    Space space = cloudDrive.getSpace();
    FileDetail recycler = this.fileService.getRecycleBin(space.getVFolder().getId());
    return IdUtils.toKey("space", space.getId(), recycler.getId());
  }
}
