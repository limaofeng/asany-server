package cn.asany.shanhai.gateway.util;

import java.util.List;
import lombok.Builder;
import lombok.Data;

/** @author limaofeng */
@Builder
@Data
public class DiffObject {
  private String path;
  private DiffStatus status;
  private Object prev;
  private Object current;

  private List<DiffObject> diffObjects;

  public enum DiffStatus {
    M,
    D,
    A
  }
}
