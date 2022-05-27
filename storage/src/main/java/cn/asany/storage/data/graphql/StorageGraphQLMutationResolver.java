package cn.asany.storage.data.graphql;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.UploadService;
import cn.asany.storage.data.domain.FileDetail;
import cn.asany.storage.data.domain.FileLabel;
import cn.asany.storage.data.service.FileService;
import cn.asany.storage.data.service.StorageService;
import cn.asany.storage.data.util.IdUtils;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.error.ValidationException;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.graphql.UpdateMode;
import org.springframework.stereotype.Component;

/**
 * 文件上传
 *
 * @author limaofeng
 */
@Slf4j
@Component
public class StorageGraphQLMutationResolver implements GraphQLMutationResolver {

  private final StorageService storageService;
  private final FileService fileService;

  public StorageGraphQLMutationResolver(
      FileService fileService, UploadService uploadService, StorageService storageService) {
    this.fileService = fileService;
    this.storageService = storageService;
  }

  public Boolean storageReindex(String id) {
    this.storageService.reindex(id);
    return true;
  }

  public FileObject renameFile(String key, String name) {
    IdUtils.FileKey fileKey = IdUtils.parseKey(key);

    return fileService.renameFile(fileKey.getFile().getId(), name).toFileObject(fileKey.getSpace());
  }

  public FileObject createFolder(String name, String parentFolder) {
    IdUtils.FileKey fileKey = IdUtils.parseKey(parentFolder);

    return fileService
        .createFolder(name, fileKey.getFile().getId())
        .toFileObject(fileKey.getSpace());
  }

  public List<FileObject> addStarForFiles(List<String> keys, UpdateMode mode) {
    List<FileDetail> fileDetails = new ArrayList<>();

    Optional<String> firstKey = keys.stream().findFirst();

    if (!firstKey.isPresent()) {
      return new ArrayList<>();
    }

    IdUtils.FileKey firstFileKey = IdUtils.parseKey(firstKey.get());

    for (String key : keys) {
      IdUtils.FileKey fileKey = IdUtils.parseKey(key);

      FileDetail fileDetail = this.fileService.getFileById(fileKey.getFile().getId());
      Set<FileLabel> labels = fileDetail.getLabels();
      FileLabel label = ObjectUtil.remove(labels, "name", "starred");
      if (label == null) {
        label = FileLabel.builder().file(fileDetail).name("starred").value("0").build();
      }
      if (UpdateMode.ADD == mode) {
        label.setValue("1");
        labels.add(label);
      } else if (UpdateMode.REMOVE == mode) {
        label.setValue("0");
      }
      this.fileService.update(fileDetail);
      fileDetails.add(fileDetail);
    }
    return fileDetails.stream()
        .map(item -> item.toFileObject(firstFileKey.getSpace()))
        .collect(Collectors.toList());
  }

  public List<FileObject> moveFilesToTrash(List<String> keys) {
    List<FileDetail> fileDetails = new ArrayList<>();

    Optional<String> firstKey = keys.stream().findFirst();

    if (!firstKey.isPresent()) {
      return new ArrayList<>();
    }

    IdUtils.FileKey firstFileKey = IdUtils.parseKey(firstKey.get());

    for (String key : keys) {
      IdUtils.FileKey fileKey = IdUtils.parseKey(key);

      FileDetail folder = fileService.getRecycleBin(fileKey.getRootFolder().getId());

      FileDetail fileDetail = this.fileService.getFileById(fileKey.getFile().getId());

      Set<FileLabel> labels = fileDetail.getLabels();

      if (ObjectUtil.exists(labels, "name", "original_path")) {
        continue;
      }

      FileDetail parentFile = this.fileService.getFileById(fileDetail.getParentFile().getId());
      labels.add(
          FileLabel.builder()
              .file(fileDetail)
              .name("original_path")
              .value(parentFile.getPath())
              .build());

      fileDetails.add(this.fileService.move(fileDetail, folder));
    }

    return fileDetails.stream()
        .map(item -> item.toFileObject(firstFileKey.getSpace()))
        .collect(Collectors.toList());
  }

  public List<FileObject> deleteFiles(List<String> keys) {
    List<FileDetail> fileDetails = new ArrayList<>();

    Optional<String> firstKey = keys.stream().findFirst();

    if (!firstKey.isPresent()) {
      return new ArrayList<>();
    }

    IdUtils.FileKey firstFileKey = IdUtils.parseKey(firstKey.get());

    for (String key : keys) {
      IdUtils.FileKey fileKey = IdUtils.parseKey(key);

      FileDetail fileDetail = this.fileService.getFileById(fileKey.getFile().getId());

      this.fileService.delete(fileDetail.getId());
      fileDetails.add(fileDetail);
    }

    return fileDetails.stream()
        .map(item -> item.toFileObject(firstFileKey.getSpace()))
        .collect(Collectors.toList());
  }

  public List<FileObject> restoreFiles(List<String> keys) {
    List<FileDetail> fileDetails = new ArrayList<>();

    Optional<String> firstKey = keys.stream().findFirst();

    if (!firstKey.isPresent()) {
      return new ArrayList<>();
    }

    IdUtils.FileKey firstFileKey = IdUtils.parseKey(firstKey.get());

    for (String key : keys) {
      IdUtils.FileKey fileKey = IdUtils.parseKey(key);

      FileDetail fileDetail = this.fileService.getFileById(fileKey.getFile().getId());

      Set<FileLabel> labels = fileDetail.getLabels();

      FileLabel label = ObjectUtil.remove(labels, "name", "original_path");

      if (label == null) {
        continue;
      }

      String originalPath = label.getValue();

      Optional<FileDetail> folderOptional = fileService.closest(originalPath);

      if (!folderOptional.isPresent()) {
        throw new ValidationException("原始路径错误[" + originalPath + "],找不到可以还原的位置");
      }

      fileDetails.add(this.fileService.move(fileDetail, folderOptional.get()));
    }

    return fileDetails.stream()
        .map(item -> item.toFileObject(firstFileKey.getSpace()))
        .collect(Collectors.toList());
  }

  public Integer clearFilesInTrash(String key, DataFetchingEnvironment environment) {
    IdUtils.FileKey fileKey = IdUtils.parseKey(key);
    Long rootFolder = fileKey.getRootFolder().getId();
    return this.fileService.clearTrash(rootFolder);
  }
}
