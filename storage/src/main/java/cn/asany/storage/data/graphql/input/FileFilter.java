package cn.asany.storage.data.graphql.input;

import cn.asany.storage.data.domain.FileDetail;
import cn.asany.storage.data.util.IdUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.inputs.QueryFilter;

@Data
@EqualsAndHashCode(callSuper = true)
public class FileFilter extends QueryFilter<FileFilter, FileDetail> {

  private AcceptFolder folder;

  public void setStarred(Boolean starred) {
    this.builder.equal("labels.name", "starred");
  }

  public void setTrashed(Boolean trashed) {
    this.builder.equal("labels.name", "trashed");
  }

  public void setFolder(AcceptFolder acceptFolder) {
    IdUtils.FileKey fileKey = IdUtils.parseKey(acceptFolder.getId());
    if (acceptFolder.getSubfolders()) {
      this.builder
          .startsWith("path", fileKey.getFile().getPath())
          .notEqual("id", fileKey.getFile().getId());
    } else {
      this.builder.equal("parentFile.id", fileKey.getFile().getId());
    }
    // 访问跟目录时，屏蔽回收站内容
    //    if (fileKey.getPath().equals(fileKey.getRootPath())) {
    //      this.builder.notStartsWith(
    //          "path",
    //          fileKey.getRootPath() + FileDetail.NAME_OF_THE_RECYCLE_BIN + FileObject.SEPARATOR);
    //    }
  }
}
