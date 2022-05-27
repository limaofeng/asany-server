package cn.asany.storage.core.engine.virtual;

import cn.asany.storage.api.*;
import cn.asany.storage.core.StorageResolver;
import cn.asany.storage.data.domain.FileLabel;
import cn.asany.storage.data.domain.Space;
import cn.asany.storage.data.service.FileService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import lombok.Builder;
import lombok.Getter;
import org.jfantasy.framework.spring.SpringBeanUtils;

@Getter
@Builder
@JsonIgnoreProperties({
  "namePath",
  "storage",
  "storePath",
  "labels",
  "metadata",
  "inputStream",
  "rootFolder",
  "parentFile"
})
public class VirtualFileObject implements FileObject {
  private Long id;
  private String name;
  private boolean directory;
  private long size;
  private String mimeType;
  private String path;
  private Date lastModified;
  private FileObjectMetadata metadata;
  private String extension;
  private String description;
  private Date createdAt;
  private Set<FileLabel> labels;
  private VirtualStorage storage;
  private String md5;
  private String storePath;

  @Override
  public FileObject getParentFile() {
    if (isRootFolder()) {
      return null;
    }
    String _path = this.getPath();
    String parentPath =
        this.directory ? _path.replaceFirst("[^/]+/$", "") : _path.replaceFirst("[^/]+$", "");
    if (FileObject.ROOT_PATH.equals(parentPath)) {
      return null;
    }
    return this.storage.getFileItem(parentPath);
  }

  @Override
  public List<FileObject> listFiles() {
    if (!this.isDirectory()) {
      return new ArrayList<>();
    }
    return this.storage.listFiles(this.path);
  }

  public String getOriginalPath() {
    return this.path;
  }

  public String getPath() {
    if (storage == null) {
      return this.getOriginalPath();
    }
    return this.getOriginalPath().substring(storage.getRootPath().length() - 1);
  }

  public String getNamePath() {
    return this.storage.getNamePath(this);
  }

  @Override
  public Date lastModified() {
    return this.lastModified;
  }

  @Override
  public List<FileObject> listFiles(FileItemFilter filter) {
    return null;
  }

  @Override
  public List<FileObject> listFiles(FileItemSelector selector) {
    if (!this.isDirectory()) {
      return Collections.emptyList();
    }
    return FileObject.Util.flat(this.listFiles(), selector);
  }

  public boolean isRootFolder() {
    return storage.getRootFolder().getId().equals(this.id);
  }

  @Override
  public Storage getStorage() {
    return this.storage;
  }

  @Override
  public InputStream getInputStream() throws IOException {
    return this.storage.getRealFileItem(this.getPath()).getInputStream();
  }

  @Override
  public InputStream getInputStream(long start, long end) throws IOException {
    return this.storage.getRealFileItem(this.getPath()).getInputStream(start, end);
  }

  public static class VirtualFileObjectBuilder {
    public VirtualFileObject.VirtualFileObjectBuilder metadata(String etag) {
      this.md5 = etag;
      this.metadata =
          FileObjectMetadata.builder()
              .contentLength(this.size)
              .dir(this.directory)
              .contentType(this.mimeType)
              .build();
      this.metadata.setETag(etag);
      return this;
    }

    public VirtualFileObject.VirtualFileObjectBuilder storage(VirtualStorage storage) {
      this.storage = storage;
      return this;
    }

    public VirtualFileObject.VirtualFileObjectBuilder storage(StorageSpace space, String id) {
      StorageResolver storageResolver = SpringBeanUtils.getBean(StorageResolver.class);
      FileService fileService = SpringBeanUtils.getBean(FileService.class);

      this.storage = new VirtualStorage((Space) space, storageResolver, fileService);
      return this;
    }

    public VirtualFileObject.VirtualFileObjectBuilder addUserMetadata(String key, Object value) {
      this.metadata.addUserMetadata(key, value);
      return this;
    }
  }
}
