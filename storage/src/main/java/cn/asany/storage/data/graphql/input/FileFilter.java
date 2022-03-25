package cn.asany.storage.data.graphql.input;

import cn.asany.storage.data.bean.FileDetail;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.inputs.QueryFilter;

@Data
@EqualsAndHashCode(callSuper = true)
public class FileFilter extends QueryFilter<FileFilter, FileDetail> {
  private boolean recursive;

  public void setStarred(Boolean starred) {
    this.getBuilder().equal("labels.name", "starred");
  }

  public void setTrashed(Boolean trashed) {
    this.getBuilder().equal("labels.name", "trashed");
  }
}
