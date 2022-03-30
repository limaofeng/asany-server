package cn.asany.storage.data.util;

import cn.asany.base.utils.Hashids;
import cn.asany.storage.data.bean.FileDetail;
import cn.asany.storage.data.bean.Space;
import cn.asany.storage.data.service.FileService;
import cn.asany.storage.data.service.SpaceService;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;
import org.jfantasy.framework.error.ValidationException;
import org.jfantasy.framework.spring.SpringBeanUtils;
import org.jfantasy.framework.util.common.StringUtil;

public class IdUtils {

  public static String toKey(String type, String space, Long id) {
    return Hashids.toId(type + "." + space + "." + id);
  }

  public static FileKey parseKey(String folderId) {
    String key = Hashids.parseId(folderId);
    String[] ids = StringUtil.tokenizeToStringArray(key, ".");
    String type = ids[0];

    FileKey.FileKeyBuilder builder = FileKey.builder().source(key);
    if ("space".equals(type)) {
      Space space = SpringBeanUtils.getBean(SpaceService.class).get(ids[1]);
      builder
          .type("space")
          .space(space.getId())
          .storage(space.getStorage().getId())
          .path(space.getPath())
          .storePath(space.getPath())
          .isFile(false)
          .rootPath(space.getPath());

      if (ids.length > 2) {
        FileDetail file =
            SpringBeanUtils.getBean(FileService.class).getFileById(Long.parseLong(ids[2]));
        builder
            .path(file.getPath())
            .isFile(!file.getIsDirectory())
            .fileId(file.getId())
            .storePath(file.getStorePath());
      } else {
        Optional<FileDetail> optionalFileDetail =
            SpringBeanUtils.getBean(FileService.class)
                .findByPath(space.getStorage().getId(), space.getPath());
        FileDetail file = optionalFileDetail.orElseThrow(() -> new ValidationException("文件夹不存在"));
        builder.fileId(file.getId()).isFile(!file.getIsDirectory());
      }

    } else if ("file".equals(type)) {
      FileDetail file =
          SpringBeanUtils.getBean(FileService.class).getFileById(Long.parseLong(ids[1]));
      builder
          .type("file")
          .storage(file.getStorageConfig().getId())
          .path(file.getPath())
          .isFile(!file.getIsDirectory())
          .storePath(file.getStorePath())
          .fileId(file.getId());
    }
    return builder.build();
  }

  /**
   * 转换ID
   *
   * @param type space | file | chunk
   * @param id ID
   * @return String
   */
  public static String toKey(String type, String id) {
    return Hashids.toId(type + "." + id);
  }

  public static String toUploadId(Long id) {
    return Hashids.toId(String.valueOf(id));
  }

  public static Long parseUploadId(String id) {
    return Long.valueOf(Hashids.parseId(id));
  }

  @Data
  @Builder
  public static class FileKey {
    private String source;
    private String type;
    private String storage;
    private String space;
    private String rootPath;
    private Long fileId;
    private String path;
    private String storePath;
    private boolean isFile;
  }
}
