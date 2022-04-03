package cn.asany.storage.data.graphql.resolver;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.StorageSpace;
import cn.asany.storage.core.engine.virtual.VirtualFileObject;
import cn.asany.storage.core.engine.virtual.VirtualStorage;
import cn.asany.storage.data.util.IdUtils;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
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

    if (fileObject instanceof VirtualFileObject) {
      StorageSpace space = ((VirtualStorage) fileObject.getStorage()).getSpace();
      return IdUtils.toKey("space", space.getId(), ((VirtualFileObject) fileObject).getId());
    }

    String __id = (String) fileObject.getMetadata().getUserMetadata().get("__ID");

    if (StringUtil.isNotBlank(__id)) {
      return __id;
    }

    Long id = OGNL_UTIL.getValue("id", fileObject);
    return IdUtils.toKey("file", id.toString());
  }

  public String name(FileObject fileObject) {
    if (fileObject.getName() == null && FileObject.ROOT_PATH.equals(fileObject.getPath())) {
      return "";
    }
    return fileObject.getName();
  }

  public String etag(FileObject fileObject) {
    if (fileObject instanceof VirtualFileObject) {
      return ((VirtualFileObject) fileObject).getMd5();
    }
    return fileObject.getMetadata().getETag();
  }

  public Date createdAt(FileObject fileObject) {
    if (fileObject instanceof VirtualFileObject) {
      return ((VirtualFileObject) fileObject).getCreatedAt();
    }
    return (Date) fileObject.getMetadata().getUserMetadata().get("CREATED_AT");
  }

  public String description(FileObject fileObject) {
    if (fileObject instanceof VirtualFileObject) {
      return ((VirtualFileObject) fileObject).getDescription();
    }
    return (String) fileObject.getMetadata().getUserMetadata().get("DESCRIPTION");
  }

  public String extension(FileObject fileObject) {
    if (fileObject instanceof VirtualFileObject) {
      return ((VirtualFileObject) fileObject).getExtension();
    }
    return (String) fileObject.getMetadata().getUserMetadata().get("EXTENSION");
  }

  public Boolean starred(FileObject fileObject) {
    if (fileObject instanceof VirtualFileObject) {
      return ObjectUtil.exists(((VirtualFileObject) fileObject).getLabels(), "name", "starred");
    }
    return ObjectUtil.defaultValue(
        (Boolean) fileObject.getMetadata().getUserMetadata().get("IS_STARRED"), Boolean.FALSE);
  }

  public Boolean isRootFolder(FileObject fileObject) {
    if (fileObject instanceof VirtualFileObject) {
      return ((VirtualFileObject) fileObject).isRootFolder();
    }
    return "/".equals(fileObject.getPath());
  }

  public FileObject parentFolder(FileObject fileObject) {
    return fileObject.getParentFile();
  }

  public List<FileObject> parents(FileObject fileObject, DataFetchingEnvironment environment) {

    List<FileObject> parents = new ArrayList<>();

    FileObject parent = fileObject.getParentFile();
    while (parent != null) {
      parents.add(parent);
      parent = parent.getParentFile();
    }
    return parents;
  }
}
