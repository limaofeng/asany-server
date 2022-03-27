package cn.asany.storage.core;

import cn.asany.storage.api.*;
import cn.asany.storage.data.bean.MultipartUpload;
import cn.asany.storage.data.bean.Space;
import cn.asany.storage.data.service.FileService;
import cn.asany.storage.data.service.MultipartUploadService;
import cn.asany.storage.data.util.IdUtils;
import cn.asany.storage.plugin.BaseStoragePlugin;
import cn.asany.storage.plugin.MultipartStoragePlugin;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.framework.spring.SpringBeanUtils;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StreamUtil;
import org.jfantasy.framework.util.common.StringUtil;
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
  private final MultipartUploadService multipartUploadService;
  private final StorageResolver storageResolver;

  private final Map<String, StoragePlugin> plugins = new HashedMap();

  @Autowired
  public FileUploadService(
      FileService fileService,
      MultipartUploadService multipartUploadService,
      StorageResolver storageResolver) {
    this.fileService = fileService;
    this.multipartUploadService = multipartUploadService;
    this.storageResolver = storageResolver;
    StoragePlugin multiPart =
        SpringBeanUtils.createBean(
            MultipartStoragePlugin.class, SpringBeanUtils.AutoType.AUTOWIRE_BY_TYPE);
    StoragePlugin base =
        SpringBeanUtils.createBean(
            BaseStoragePlugin.class, SpringBeanUtils.AutoType.AUTOWIRE_BY_TYPE);
    plugins.put("multi-part", multiPart);
    plugins.put("base", base);
  }

  @Override
  public FileObject upload(FileObject file, UploadOptions options) throws UploadException {
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
      Storage storage = this.storageResolver.resolve(space.getStorage().getId());
      // 插件
      Set<String> plugins =
          new LinkedHashSet<>(ObjectUtil.defaultValue(space.getPlugins(), LinkedHashSet::new));
      // 添加默认插件
      plugins.add("multi-part");
      plugins.add("base");

      // 构建上传上下文对象
      UploadContext context =
          UploadContext.builder()
              .uploadService(this)
              .object(file)
              .file(temp)
              .space(space)
              .location(space.getPath())
              .storage(storage)
              .storageId(space.getStorage().getId())
              .options(options)
              .build();

      List<StoragePlugin> storagePlugins = new ArrayList<>();
      // 循环执行插件
      for (String pluginKey : plugins) {
        StoragePlugin plugin = this.plugins.get(pluginKey);
        if (!plugin.supports(context)) {
          continue;
        }
        storagePlugins.add(plugin);
      }

      Invocation invocation = new DefaultInvocation(context, storagePlugins.iterator());
      FileObject object = invocation.invoke();

      // 如果上传成功，返回对象
      if (object != null) {
        return object;
      }

      throw new IOException("出现不可预见的错误");
    } catch (IOException e) {
      throw new UploadException(e.getMessage());
    } finally {
      if (temp != null) {
        FileUtil.delFile(temp);
      }
    }
  }

  @Override
  public String initiateMultipartUpload(String name, MultipartUploadOptions options)
      throws UploadException {
    try {
      // 将要上传的位置
      Space space = this.fileService.getSpace(options.getSpace());
      // 存储器
      Storage storage = this.storageResolver.resolve(space.getStorage().getId());

      if (!storage.isMultipartUploadSupported()) {
        throw new UploadException("存储不支持分段上传");
      }

      String path = name;
      if (StringUtil.isNotBlank(options.getFolder())) {
        IdUtils.FileKey fileKey = IdUtils.parseKey(options.getFolder());
        if (!fileKey.getRootPath().equals(space.getPath())) {
          throw new UploadException("文件夹位置错误");
        }
        path = fileKey.getPath() + name;
      } else {
        // TODO: 通过策略匹配具体位置
        path = name;
      }

      Optional<MultipartUpload> prevMultipartUploadOptional =
          multipartUploadService.findMultipartUploadByPath(path, storage.getId());

      MultipartUpload prevMultipartUpload = prevMultipartUploadOptional.orElse(null);

      if (prevMultipartUpload != null
          && options.getHash().equals(prevMultipartUpload.getHash())
          && options.getChunkSize().equals(prevMultipartUpload.getChunkSize())) {
        return IdUtils.toUploadId(prevMultipartUpload.getId());
      }

      FileObjectMetadata objectMetadata = new FileObjectMetadata();

      String uploadId = storage.multipartUpload().initiate(path, objectMetadata);

      MultipartUpload multipartUpload = prevMultipartUpload;
      if (multipartUpload == null) {
        multipartUpload =
            multipartUploadService.initiateMultipartUpload(
                name,
                path,
                uploadId,
                options.getHash(),
                storage.getId(),
                options.getChunkSize(),
                options.getChunkLength());
      } else {

        storage.multipartUpload().abort(multipartUpload.getUploadId());

        multipartUpload.setName(name);
        multipartUpload.setUploadId(uploadId);
        multipartUpload.setHash(options.getHash());
        multipartUpload.setChunkSize(options.getChunkSize());
        multipartUpload.setChunkLength(options.getChunkLength());
        multipartUpload.setUploadedParts(0);
        multipartUpload.getChunks().clear();
        multipartUploadService.updateMultipartUpload(multipartUpload);
      }

      return IdUtils.toUploadId(multipartUpload.getId());

    } catch (IOException e) {
      throw new UploadException(e.getMessage());
    }
  }
}
