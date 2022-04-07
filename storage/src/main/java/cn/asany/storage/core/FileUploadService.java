package cn.asany.storage.core;

import cn.asany.storage.api.*;
import cn.asany.storage.core.engine.virtual.VirtualFileObject;
import cn.asany.storage.data.bean.FileDetail;
import cn.asany.storage.data.bean.MultipartUpload;
import cn.asany.storage.data.bean.MultipartUploadChunk;
import cn.asany.storage.data.bean.Space;
import cn.asany.storage.data.service.FileService;
import cn.asany.storage.data.service.MultipartUploadService;
import cn.asany.storage.data.util.IdUtils;
import cn.asany.storage.plugin.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StreamUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.common.file.FileUtil;
import org.jfantasy.framework.util.web.WebUtil;
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

  private Invocation createInvocation(Space space, UploadOptions options, UploadFileObject file) {

    // 插件
    Set<String> plugins = new LinkedHashSet<>();

    // 负责删除上传的物理文件
    plugins.add(CleanPlugin.ID);

    // 存储空间配置的插件
    plugins.addAll(ObjectUtil.defaultValue(space.getPlugins(), LinkedHashSet::new));

    // 添加默认插件
    plugins.add(MatchFolderPlugin.ID);
    plugins.add(FindFolderPlugin.ID);
    plugins.add(StorePathPlugin.ID);
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
            .rootFolder(space.getFolder())
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

      Invocation invocation = createInvocation(space, options, uploadFileObject);

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
  public String initiateMultipartUpload(MultipartUploadOptions options) throws UploadException {
    try {
      // 将要上传的位置
      Space space = this.fileService.getSpace(options.getSpace());

      UploadOptions uploadOptions =
          UploadOptions.builder().space(space.getId()).folder(options.getFolder()).build();

      FileObjectMetadata metadata =
          FileObjectMetadata.builder()
              .contentLength(options.getSize())
              .contentType(options.getMimeType())
              .build();

      Invocation invocation =
          createInvocation(space, uploadOptions, new UploadFileObject(metadata));

      invocation.invoke();

      UploadContext context = invocation.getContext();

      String storePath = context.getStorePath();
      String filename = context.getFilename();
      Storage storage = context.getStorage();

      if (!storage.isMultipartUploadSupported()) {
        throw new UploadException("存储不支持分段上传");
      }

      Optional<MultipartUpload> prevMultipartUploadOptional =
          multipartUploadService.findMultipartUploadByHash(
              options.getHash(), space.getId(), storage.getId());

      MultipartUpload prevMultipartUpload = prevMultipartUploadOptional.orElse(null);

      if (prevMultipartUpload != null
          && options.getHash().equals(prevMultipartUpload.getHash())
          && options.getChunkSize().equals(prevMultipartUpload.getChunkSize())) {
        return IdUtils.toUploadId(prevMultipartUpload.getId());
      }

      String uploadId = storage.multipartUpload().initiate(storePath, metadata);

      MultipartUpload multipartUpload = prevMultipartUpload;
      if (multipartUpload == null) {
        multipartUpload =
            multipartUploadService.initiateMultipartUpload(
                storePath,
                space.getId(),
                uploadId,
                options.getHash(),
                storage.getId(),
                options.getChunkSize(),
                options.getChunkLength(),
                metadata);
      } else {

        storage.multipartUpload().abort(multipartUpload.getUploadId());

        multipartUpload.setPath(storePath);
        multipartUpload.setSpace(space.getId());
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

  public FileObject completeMultipartUpload(String uploadId, String name, String folderKey)
      throws UploadException {
    try {
      Long id = IdUtils.parseUploadId(uploadId);

      MultipartUpload multipartUpload = this.multipartUploadService.get(id);

      Storage storage = storageResolver.resolve(multipartUpload.getStorage());
      Space space = this.fileService.getSpace(multipartUpload.getSpace());

      if (!Objects.equals(multipartUpload.getChunkLength(), multipartUpload.getUploadedParts())) {
        throw new UploadException("文件不完整");
      }

      List<MultipartUploadChunk> chunks = multipartUpload.getChunks();
      List<String> partETags =
          ObjectUtil.sort(chunks, "index", "asc").stream()
              .map(MultipartUploadChunk::getEtag)
              .collect(Collectors.toList());

      FileObject object = storage.getFileItem(multipartUpload.getPath());

      if (object == null) {
        storage
            .multipartUpload()
            .complete(multipartUpload.getPath(), multipartUpload.getUploadId(), partETags);
        object = storage.getFileItem(multipartUpload.getPath());
      }

      FileObjectMetadata metadata =
          FileObjectMetadata.builder()
              .contentLength(multipartUpload.getSize())
              .contentType(multipartUpload.getMimeType())
              .build();

      UploadOptions uploadOptions =
          UploadOptions.builder().space(space.getId()).folder(folderKey).build();

      Invocation invocation =
          createInvocation(space, uploadOptions, new UploadFileObject(name, metadata));

      invocation.invoke();

      UploadContext context = invocation.getContext();

      VirtualFileObject folder = (VirtualFileObject) context.getFolder();
      String filename = context.getFilename();
      String extension = WebUtil.getExtension(multipartUpload.getMimeType());

      FileDetail fileDetail =
          fileService.createFile(
              folder.getOriginalPath() + StringUtil.uuid() + extension,
              filename,
              object.getMetadata().getContentType(),
              object.getSize(),
              object.getMetadata().getETag(),
              storage.getId(),
              multipartUpload.getPath(),
              "",
              folder.getId());

      return fileDetail.toFileObject(space);
    } catch (IOException e) {
      throw new UploadException(e.getMessage());
    }
  }
}
