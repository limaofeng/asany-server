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
package cn.asany.storage.core;

import cn.asany.storage.api.*;
import cn.asany.storage.core.engine.virtual.VirtualFileObject;
import cn.asany.storage.data.domain.FileDetail;
import cn.asany.storage.data.domain.MultipartUpload;
import cn.asany.storage.data.domain.MultipartUploadChunk;
import cn.asany.storage.data.domain.Space;
import cn.asany.storage.data.service.FileService;
import cn.asany.storage.data.service.MultipartUploadService;
import cn.asany.storage.data.util.IdUtils;
import cn.asany.storage.plugin.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import net.asany.jfantasy.framework.util.common.StreamUtil;
import net.asany.jfantasy.framework.util.common.StringUtil;
import net.asany.jfantasy.framework.util.common.file.FileUtil;
import net.asany.jfantasy.framework.util.web.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 文件上传服务
 *
 * @author limaofeng
 */
@Slf4j
@Component
public class FileUploadService implements UploadService {

  private final FileService fileService;
  private final MultipartUploadService multipartUploadService;
  private final StorageResolver storageResolver;

  private final Map<String, StoragePlugin> pluginMap = new LinkedHashMap<>();

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
    Path temp = null;
    UploadFileObject uploadFileObject;
    try {
      // 获取临时文件
      if (file instanceof UploadFileObject) {
        temp = ((UploadFileObject) file).getFile().toPath();
        uploadFileObject = ((UploadFileObject) file);
      } else {
        temp = FileUtil.tmp();
        StreamUtil.copyThenClose(file.getInputStream(), Files.newOutputStream(temp));
        uploadFileObject = new UploadFileObject(file.getName(), temp.toFile(), file.getMetadata());
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
        try {
          FileUtil.rm(temp);
        } catch (IOException e) {
          log.error(e.getMessage(), e);
        }
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

  @Override
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
      List<String> partEtags =
          ObjectUtil.sort(chunks, "index", "asc").stream()
              .map(MultipartUploadChunk::getEtag)
              .collect(Collectors.toList());

      FileObject object = storage.getFileItem(multipartUpload.getPath());

      if (object == null) {
        storage
            .multipartUpload()
            .complete(multipartUpload.getPath(), multipartUpload.getUploadId(), partEtags);
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
