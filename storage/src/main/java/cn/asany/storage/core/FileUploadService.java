package cn.asany.storage.core;

import cn.asany.storage.api.*;
import cn.asany.storage.data.bean.Space;
import cn.asany.storage.data.service.FileService;
import cn.asany.storage.plugin.BaseStoragePlugin;
import cn.asany.storage.plugin.FragmentStoragePlugin;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StreamUtil;
import org.jfantasy.framework.util.common.file.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 文件上传服务
 *
 * @author limaofeng
 */
@Component
public class FileUploadService implements UploadService {

  private static final Log LOG = LogFactory.getLog(FileUploadService.class);

  private static final String SEPARATOR = File.separator;
  private final FileService fileService;
  private final StorageResolver storageResolver;

  private Map<String, StoragePlugin> plugins = new HashedMap();

  @Autowired
  public FileUploadService(FileService fileService, StorageResolver storageResolver) {
    this.fileService = fileService;
    this.storageResolver = storageResolver;
    StoragePlugin fragment =
        SpringContextUtil.createBean(
            FragmentStoragePlugin.class, SpringContextUtil.AutoType.AUTOWIRE_BY_TYPE);
    StoragePlugin base =
        SpringContextUtil.createBean(
            BaseStoragePlugin.class, SpringContextUtil.AutoType.AUTOWIRE_BY_TYPE);
    plugins.put("fragment", fragment);
    plugins.put("base", base);
  }

  @Override
  public FileObject upload(FileObject file, UploadOptions options) throws IOException {
    File temp = null;
    try {
      // 获取临时文件
      if (file instanceof UploadFileObject) {
        temp = ((UploadFileObject) file).getFile();
      } else {
        temp = FileUtil.tmp();
        StreamUtil.copyThenClose(file.getInputStream(), new FileOutputStream(temp));
      }
      // 将要上传的位置
      Space space = this.fileService.getSpace(options.getSpace());
      // 存储器
      Storage storage = this.storageResolver.resolve(space.getId());
      // 插件
      Set<String> plugins =
          new HashSet<>(ObjectUtil.defaultValue(space.getPlugins(), HashSet::new));
      // 添加默认插件
      plugins.add("base");

      // 构建上传上下文对象
      UploadContext context =
          UploadContext.builder()
              .uploadService(this)
              .object(file)
              .file(temp)
              .location(space.getPath())
              .storage(storage)
              .storageId(space.getStorage().getId())
              .options(options)
              .build();

      // 循环执行插件
      for (String pluginKey : plugins) {
        StoragePlugin plugin = this.plugins.get(pluginKey);
        if (!plugin.supports(context)) {
          continue;
        }
        FileObject object = plugin.upload(context);
        // 如果上传成功，返回对象
        if (context.isUploaded()) {
          return object;
        }
      }
      throw new IOException("出现不可预见的错误");
    } finally {
      if (temp != null) {
        FileUtil.delFile(temp);
      }
    }
  }
}
