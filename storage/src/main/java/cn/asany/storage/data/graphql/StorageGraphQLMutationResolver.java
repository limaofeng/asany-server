package cn.asany.storage.data.graphql;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.UploadService;
import cn.asany.storage.data.bean.FileDetail;
import cn.asany.storage.data.bean.FileLabel;
import cn.asany.storage.data.service.FileService;
import cn.asany.storage.data.service.StorageService;
import cn.asany.storage.data.util.IdUtils;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.graphql.UpdateMode;
import org.jfantasy.graphql.context.AuthorizationGraphQLServletContext;
import org.springframework.stereotype.Component;

/**
 * 文件上传
 *
 * @author limaofeng
 */
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

  public FileObject renameFile(String key, String name, DataFetchingEnvironment environment) {
    IdUtils.FileKey fileKey = IdUtils.parseKey(key);

    AuthorizationGraphQLServletContext context = environment.getContext();

    context.setAttribute("QUERY_ROOT_FILE_KEY", fileKey);
    context.setAttribute("QUERY_ROOT_PATH", fileKey.getRootPath());

    return fileService.renameFile(fileKey.getFileId(), name).toFileObject();
  }

  public FileObject createFolder(
      String name, String parentFolder, DataFetchingEnvironment environment) {
    IdUtils.FileKey fileKey = IdUtils.parseKey(parentFolder);

    AuthorizationGraphQLServletContext context = environment.getContext();

    context.setAttribute("QUERY_ROOT_FILE_KEY", fileKey);
    context.setAttribute("QUERY_ROOT_PATH", fileKey.getRootPath());

    return fileService.createFolder(name, fileKey.getFileId()).toFileObject();
  }

  public List<FileObject> addStarForFiles(
      List<String> keys, UpdateMode mode, DataFetchingEnvironment environment) {
    List<FileDetail> fileDetails = new ArrayList<>();

    AuthorizationGraphQLServletContext context = environment.getContext();

    for (String key : keys) {
      IdUtils.FileKey fileKey = IdUtils.parseKey(key);

      context.setAttribute("QUERY_ROOT_FILE_KEY", fileKey);
      context.setAttribute("QUERY_ROOT_PATH", fileKey.getRootPath());

      FileDetail fileDetail = this.fileService.getFileById(fileKey.getFileId());
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
    return fileDetails.stream().map(FileDetail::toFileObject).collect(Collectors.toList());
  }

  public List<FileObject> moveFilesToTrash(List<String> keys, DataFetchingEnvironment environment) {
    List<FileDetail> fileDetails = new ArrayList<>();

    AuthorizationGraphQLServletContext context = environment.getContext();

    for (String key : keys) {
      IdUtils.FileKey fileKey = IdUtils.parseKey(key);

      FileDetail folder = fileService.getRecycler(fileKey.getStorage(), fileKey.getRootPath());

      context.setAttribute("QUERY_ROOT_FILE_KEY", fileKey);
      context.setAttribute("QUERY_ROOT_PATH", fileKey.getRootPath());

      FileDetail fileDetail = this.fileService.getFileById(fileKey.getFileId());

      Set<FileLabel> labels = fileDetail.getLabels();

      if (ObjectUtil.exists(labels, "name", "original_path")) {
        continue;
      }

      labels.add(
          FileLabel.builder()
              .file(fileDetail)
              .name("original_path")
              .value(fileDetail.getParentFile().getPath())
              .build());

      fileDetails.add(this.fileService.move(fileDetail, folder));
    }

    return fileDetails.stream().map(FileDetail::toFileObject).collect(Collectors.toList());
  }

  public List<FileObject> deleteFiles(List<String> keys, DataFetchingEnvironment environment) {
    List<FileDetail> fileDetails = new ArrayList<>();

    AuthorizationGraphQLServletContext context = environment.getContext();

    for (String key : keys) {
      IdUtils.FileKey fileKey = IdUtils.parseKey(key);

      context.setAttribute("QUERY_ROOT_FILE_KEY", fileKey);
      context.setAttribute("QUERY_ROOT_PATH", fileKey.getRootPath());

      FileDetail fileDetail = this.fileService.getFileById(fileKey.getFileId());

      this.fileService.delete(fileDetail.getId());
      fileDetails.add(fileDetail);
    }

    return fileDetails.stream().map(FileDetail::toFileObject).collect(Collectors.toList());
  }

  public List<FileObject> restoreFiles(List<String> keys, DataFetchingEnvironment environment) {
    List<FileDetail> fileDetails = new ArrayList<>();

    AuthorizationGraphQLServletContext context = environment.getContext();

    for (String key : keys) {
      IdUtils.FileKey fileKey = IdUtils.parseKey(key);

      context.setAttribute("QUERY_ROOT_FILE_KEY", fileKey);
      context.setAttribute("QUERY_ROOT_PATH", fileKey.getRootPath());

      FileDetail fileDetail = this.fileService.getFileById(fileKey.getFileId());

      Set<FileLabel> labels = fileDetail.getLabels();

      FileLabel label = ObjectUtil.remove(labels, "name", "original_path");

      if (label == null) {
        continue;
      }

      String originalPath = label.getValue();

      FileDetail folder = fileService.getFolderByPath(fileKey.getStorage(), originalPath);

      fileDetails.add(this.fileService.move(fileDetail, folder));
    }

    return fileDetails.stream().map(FileDetail::toFileObject).collect(Collectors.toList());
  }

  public Integer clearFilesInTrash(String key, DataFetchingEnvironment environment) {
    IdUtils.FileKey fileKey = IdUtils.parseKey(key);
    return this.fileService.clearTrash(fileKey.getStorage(), fileKey.getRootPath());
  }
}
