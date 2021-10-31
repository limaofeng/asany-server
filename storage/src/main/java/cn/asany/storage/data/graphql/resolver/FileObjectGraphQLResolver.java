package cn.asany.storage.data.graphql.resolver;

import cn.asany.storage.api.FileObject;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.ArrayList;
import java.util.List;
import org.jfantasy.framework.util.ognl.OgnlUtil;
import org.springframework.stereotype.Component;

/**
 * 文件对象
 *
 * @author limaofeng
 */
@Component
public class FileObjectGraphQLResolver implements GraphQLResolver<FileObject> {

  private static final OgnlUtil OGNL_UTIL = OgnlUtil.getInstance();

  public String id(FileObject fileObject) {
    Object id = OGNL_UTIL.getValue("id", fileObject);
    if (id != null) {
      return id.toString();
    }
    String storageId = OgnlUtil.getInstance().getValue("storage.id", fileObject);
    if (storageId != null) {
      return storageId + ":" + fileObject.getPath();
    }
    return fileObject.getPath();
  }

  public String name(FileObject fileObject) {
    if (fileObject.getName() == null && FileObject.ROOT_PATH.equals(fileObject.getPath())) {
      return "";
    }
    return fileObject.getName();
  }

  public List<FileObject> parents(FileObject fileObject) {
    List<FileObject> parentFiles = new ArrayList<>();
    FileObject parentFile = fileObject.getParentFile();
    while (parentFile != null) {
      parentFiles.add(parentFile);
      parentFile = parentFile.getParentFile();
    }
    return parentFiles;
  }
}
