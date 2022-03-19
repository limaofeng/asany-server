package cn.asany.storage.data.graphql.resolver;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.data.bean.FileDetail;
import cn.asany.storage.data.service.FileService;
import cn.asany.storage.data.util.IdUtils;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.ognl.OgnlUtil;
import org.jfantasy.graphql.context.AuthorizationGraphQLServletContext;
import org.springframework.stereotype.Component;

/**
 * 文件对象
 *
 * @author limaofeng
 */
@Component
public class FileObjectGraphQLResolver implements GraphQLResolver<FileObject> {

  private final FileService fileService;

  private static final OgnlUtil OGNL_UTIL = OgnlUtil.getInstance();

  public FileObjectGraphQLResolver(FileService fileService) {
    this.fileService = fileService;
  }

  public String id(FileObject fileObject, DataFetchingEnvironment environment) {
    AuthorizationGraphQLServletContext context = environment.getContext();

    IdUtils.FileKey fileKey = context.getAttribute("QUERY_ROOT_FILE_KEY");

    Long id = OGNL_UTIL.getValue("id", fileObject);
    if (fileKey == null || "file".equals(fileKey.getType())) {
      return IdUtils.toKey("file", id.toString());
    }
    return IdUtils.toKey(fileKey.getType(), fileKey.getSpace(), id);
  }

  public String name(FileObject fileObject) {
    if (fileObject.getName() == null && FileObject.ROOT_PATH.equals(fileObject.getPath())) {
      return "";
    }
    return fileObject.getName();
  }

  public String md5(FileObject fileObject) {
    return fileObject.getMetadata().getContentMD5();
  }

  public Date createdAt(FileObject fileObject) {
    return (Date) fileObject.getMetadata().getUserMetadata().get("CREATED_AT");
  }

  public String description(FileObject fileObject) {
    return (String) fileObject.getMetadata().getUserMetadata().get("DESCRIPTION");
  }

  public Boolean isRootFolder(FileObject fileObject, DataFetchingEnvironment environment) {
    AuthorizationGraphQLServletContext context = environment.getContext();

    IdUtils.FileKey fileKey = context.getAttribute("QUERY_ROOT_FILE_KEY");

    return "/".equals(fileObject.getPath()) || isRootFolderOfSpace(fileKey, fileObject.getPath());
  }

  public List<FileObject> parents(FileObject fileObject, DataFetchingEnvironment environment) {
    Long id = OGNL_UTIL.getValue("id", fileObject);

    AuthorizationGraphQLServletContext context = environment.getContext();
    String rootPath = context.getAttribute("QUERY_ROOT_PATH");

    List<FileObject> parentFiles =
        fileService.getFileParentsById(id).stream()
            .map(FileDetail::toFileObject)
            .collect(Collectors.toList());

    if (StringUtil.isNotBlank(rootPath)) {
      return ObjectUtil.filter(
          parentFiles,
          (item) -> item.getPath().startsWith(rootPath) && !item.getPath().equals(rootPath));
    }

    return parentFiles;
  }

  public static boolean isRootFolderOfSpace(IdUtils.FileKey fileKey, String path) {
    if (fileKey == null || !"space".equals(fileKey.getType())) {
      return false;
    }
    return fileKey.getRootPath().equals(path);
  }
}
