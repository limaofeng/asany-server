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
package cn.asany.drive.service;

import cn.asany.base.utils.UUID;
import cn.asany.drive.dao.CloudDriveDao;
import cn.asany.drive.domain.CloudDrive;
import cn.asany.drive.domain.CloudDriveQuota;
import cn.asany.drive.domain.enums.CloudDriveType;
import cn.asany.security.core.dao.UserDao;
import cn.asany.security.core.domain.User;
import cn.asany.storage.api.Storage;
import cn.asany.storage.data.domain.Space;
import cn.asany.storage.data.service.FileService;
import java.util.List;
import java.util.Optional;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.spring.mvc.error.NotFoundException;
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
  public CloudDrive createPersonalCloudDrive(Long uid, long quota) {
    Optional<User> optionalUser = userDao.findById(uid);
    User user = optionalUser.orElseThrow(() -> new NotFoundException("用户不存在"));
    CloudDrive cloudDrive =
        CloudDrive.builder()
            .name(PERSONAL_CLOUDDRIVE_DEFAULT_NAME)
            .type(CloudDriveType.PERSONAL)
            .ownerType(User.OWNERSHIP_KEY)
            .ownerId(uid)
            .build();
    CloudDriveQuota quota1 = CloudDriveQuota.builder().count(0).size(quota).usage(0L).build();
    cloudDrive.setQuota(quota1);
    quota1.setCloudDrive(cloudDrive);

    // TODO 应该动态读取配置
    String storage = Storage.DEFAULT_STORAGE_ID;

    Space space =
        this.fileService.createStorageSpace(
            UUID.getShortId(),
            user.getName() + "的" + PERSONAL_CLOUDDRIVE_DEFAULT_NAME,
            "/CloudDrive/" + user.getUsername() + "/",
            storage);

    cloudDrive.setSpace(space);

    return this.cloudDriveDao.save(cloudDrive);
  }

  public void deleteCloudDrive(Long id) {
    CloudDrive cloudDrive = this.cloudDriveDao.getReferenceById(id);
    this.cloudDriveDao.deleteById(id);
    this.fileService.deleteStorageSpace(cloudDrive.getSpace().getId());
  }

  public Optional<CloudDrive> findPersonalCloudDriveByUserId(Long uid) {
    PropertyFilter builder =
        PropertyFilter.newFilter()
            .equal("type", CloudDriveType.PERSONAL)
            .equal("ownerType", User.OWNERSHIP_KEY)
            .equal("ownerId", uid);
    return cloudDriveDao.findOne(builder);
  }

  public List<CloudDrive> cloudDrives(Long uid) {
    PropertyFilter filter =
        PropertyFilter.newFilter()
            .equal("type", CloudDriveType.PERSONAL)
            .equal("ownerType", User.OWNERSHIP_KEY)
            .equal("ownerId", uid);
    return cloudDriveDao.findAll(filter);
  }

  public CloudDrive getCloudDriveById(Long id) {
    return this.cloudDriveDao.getReferenceById(id);
  }
}
