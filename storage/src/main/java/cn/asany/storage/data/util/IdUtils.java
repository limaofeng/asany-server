package cn.asany.storage.data.util;

import cn.asany.base.utils.Hashids;
import cn.asany.storage.data.domain.FileDetail;
import cn.asany.storage.data.domain.Space;
import cn.asany.storage.data.service.FileService;
import cn.asany.storage.data.service.SpaceService;
import lombok.Builder;
import lombok.Data;
import net.asany.jfantasy.framework.spring.SpringBeanUtils;
import net.asany.jfantasy.framework.util.common.StringUtil;

/**
 * ID 工具类
 *
 * @author limaofeng
 */
public class IdUtils {

  public static String toKey(String type, String space, Long id) {
    return Hashids.toId(type + "." + space + "." + id);
  }

  public static FileKey parseKey(String folderId) {
    FileService fileService = SpringBeanUtils.getBean(FileService.class);

    String key = Hashids.parseId(folderId);
    String[] ids = StringUtil.tokenizeToStringArray(key, ".");
    String type = ids[0];

    FileKey.FileKeyBuilder builder = FileKey.builder().source(key);
    if ("space".equals(type)) {
      Space space = SpringBeanUtils.getBean(SpaceService.class).get(ids[1]);
      FileDetail folder = space.getVFolder();
      builder.type("space").space(space).rootFolder(folder).file(folder);

      if (ids.length > 2) {
        FileDetail file = fileService.getFileById(Long.parseLong(ids[2]));
        builder.file(file);
      }
    } else if ("file".equals(type)) {
      FileDetail file = fileService.getFileById(Long.parseLong(ids[1]));
      builder.type("file").file(file);
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
    /** 原始串 */
    private String source;

    /** Key 类型 file / space */
    private String type;

    /** 存储空间 */
    private Space space;

    /** 相对根目录 */
    private FileDetail rootFolder;

    /** 文件 */
    private FileDetail file;
  }
}
