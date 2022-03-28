package cn.asany.storage.core;

import cn.asany.storage.api.*;
import cn.asany.storage.data.bean.MultipartUpload;
import cn.asany.storage.data.bean.Space;
import cn.asany.storage.data.service.FileService;
import cn.asany.storage.data.service.MultipartUploadService;
import cn.asany.storage.data.util.IdUtils;
import cn.asany.storage.plugin.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
  private final MultipartUploadService multipartUploadService;
  private final StorageResolver storageResolver;

  private final Map<String, StoragePlugin> pluginMap = new HashedMap();

  @Autowired
  public FileUploadService(
      FileService fileService,
      MultipartUploadService multipartUploadService,
      StorageResolver storageResolver,
      List<StoragePlugin> plugins) {
    this.fileService = fileService;
    this.multipartUploadService = multipartUploadService;
    this.storageResolver = storageResolver;

    for (StoragePlugin plugin : plugins) {
      pluginMap.put(plugin.id(), plugin);
    }
  }

  private Invocation createInvocation(
      Storage storage, Space space, UploadOptions options, UploadFileObject file) {

    // 插件
    Set<String> plugins =
        new LinkedHashSet<>(ObjectUtil.defaultValue(space.getPlugins(), LinkedHashSet::new));

    // 添加默认插件
    plugins.add(MatchFolderPlugin.ID);
    plugins.add(FindFolderPlugin.ID);
    plugins.add(FileNameStrategyPlugin.ID);

    if (!file.isNoFile()) {
      plugins.add(MultipartStoragePlugin.ID);
      plugins.add(BaseStoragePlugin.ID);
    }

    // 构建上传上下文对象
    UploadContext context =
        UploadContext.builder()
            .uploadService(this)
            .file(file)
            .space(space)
            .rootFolder(space.getPath())
            .storage(storage)
            .options(options)
            .build();

    List<StoragePlugin> storagePlugins = new ArrayList<>();

    // 循环执行插件
    for (String pluginKey : plugins) {
      StoragePlugin plugin = this.pluginMap.get(pluginKey);
      if (!plugin.supports(context)) {
        continue;
      }
      storagePlugins.add(plugin);
    }

    return new DefaultInvocation(context, storagePlugins.iterator());
  }

  @Override
  public FileObject upload(FileObject file, UploadOptions options) throws UploadException {
    File temp = null;
    UploadFileObject uploadFileObject;
    try {
      // 获取临时文件
      if (file instanceof UploadFileObject) {
        temp = ((UploadFileObject) file).getFile();
        uploadFileObject = ((UploadFileObject) file);
      } else {
        temp = FileUtil.tmp();
        StreamUtil.copyThenClose(file.getInputStream(), new FileOutputStream(temp));
        uploadFileObject = new UploadFileObject(file.getName(), temp, file.getMetadata());
      }

      // 将要上传的位置
      Space space = this.fileService.getSpace(options.getSpace());

      // 存储器
      Storage storage = this.storageResolver.resolve(space.getStorage().getId());

      Invocation invocation = createInvocation(storage, space, options, uploadFileObject);

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

      UploadOptions uploadOptions =
          UploadOptions.builder().space(space.getId()).folder(options.getFolder()).build();

      FileObjectMetadata metadata =
          FileObjectMetadata.builder()
              .contentLength(options.getSize())
              .contentType(options.getMimeType())
              .build();

      Invocation invocation =
          createInvocation(storage, space, uploadOptions, new UploadFileObject(name, metadata));

      invocation.invoke();

      UploadContext context = invocation.getContext();

      String folder = context.getFolder();
      String filename = context.getFilename();

      String path = folder + filename;

      Optional<MultipartUpload> prevMultipartUploadOptional =
          multipartUploadService.findMultipartUploadByPath(path, storage.getId());

      MultipartUpload prevMultipartUpload = prevMultipartUploadOptional.orElse(null);

      if (prevMultipartUpload != null
          && options.getHash().equals(prevMultipartUpload.getHash())
          && options.getChunkSize().equals(prevMultipartUpload.getChunkSize())) {
        return IdUtils.toUploadId(prevMultipartUpload.getId());
      }

      String uploadId = storage.multipartUpload().initiate(path, metadata);

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
                options.getChunkLength(),
                metadata);
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
