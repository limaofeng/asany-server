package cn.asany.drive.service;

import cn.asany.drive.bean.CloudDrive;
import cn.asany.drive.bean.CloudDriveQuota;
import cn.asany.drive.bean.enums.CloudDriveType;
import cn.asany.drive.dao.CloudDriveDao;
import cn.asany.security.core.bean.User;
import cn.asany.security.core.dao.UserDao;
import cn.asany.storage.data.bean.Space;
import cn.asany.storage.data.service.FileService;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.spring.mvc.error.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CloudDriveService {

  private static final String PERSONAL_CLOUDDRIVE_DEFAULT_NAME = "个人网盘";

  private final UserDao userDao;
  private final CloudDriveDao cloudDriveDao;
  private final FileService fileService;

  public CloudDriveService(UserDao userDao, FileService fileService, CloudDriveDao cloudDriveDao) {
    this.userDao = userDao;
    this.cloudDriveDao = cloudDriveDao;
    this.fileService = fileService;
  }

  /**
   * 创建个人网盘
   *
   * @param uid 用户ID
   * @param quota 分配空间(单位MB)
   * @return CloudDrive
   */
  public CloudDrive createPersonalCloudDrive(Long uid, int quota) {
    Optional<User> optionalUser = userDao.findById(uid);
    User user = optionalUser.orElseThrow(() -> new NotFoundException("用户不存在"));
    CloudDrive cloudDrive =
        CloudDrive.builder()
            .name(PERSONAL_CLOUDDRIVE_DEFAULT_NAME)
            .type(CloudDriveType.PERSONAL)
            .ownerType(User.OWNERSHIP_KEY)
            .ownerId(uid)
            .build();
    CloudDriveQuota quota1 = CloudDriveQuota.builder().count(0).size(quota).usage(0).build();
    cloudDrive.setQuota(quota1);
    quota1.setCloudDrive(cloudDrive);

    // TODO 应该动态读取配置
    String storage = "Global";

    Space space =
        this.fileService.createStorageSpace(
            user.getName() + "的" + PERSONAL_CLOUDDRIVE_DEFAULT_NAME,
            "/CloudDrive/" + user.getUsername() + "/",
            storage);

    cloudDrive.setSpace(space);

    return this.cloudDriveDao.save(cloudDrive);
  }

  public void deleteCloudDrive(Long id) {
    CloudDrive cloudDrive = this.cloudDriveDao.getById(id);
    this.cloudDriveDao.deleteById(id);
    this.fileService.deleteStorageSpace(cloudDrive.getSpace().getId());
  }

  public Optional<CloudDrive> findPersonalCloudDriveByUserId(Long uid) {
    PropertyFilterBuilder builder =
        PropertyFilter.builder()
            .equal("type", CloudDriveType.PERSONAL)
            .equal("ownerType", User.OWNERSHIP_KEY)
            .equal("ownerId", uid);
    return cloudDriveDao.findOne(builder.build());
  }

  public List<CloudDrive> cloudDrives(Long uid) {
    PropertyFilterBuilder builder =
        PropertyFilter.builder()
            .equal("type", CloudDriveType.PERSONAL)
            .equal("ownerType", User.OWNERSHIP_KEY)
            .equal("ownerId", uid);
    return cloudDriveDao.findAll(builder.build());
  }

  public CloudDrive getCloudDriveById(Long id) {
    return this.cloudDriveDao.getById(id);
  }
}
