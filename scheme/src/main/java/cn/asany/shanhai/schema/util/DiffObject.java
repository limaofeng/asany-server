package cn.asany.shanhai.schema.util;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author limaofeng
 */
@Builder
@Data
public class DiffObject {
    private String path;
    private DiffStatus status;
    private Object prev;
    private Object current;

    private List<DiffObject> diffObjects;

    static enum DiffStatus {
        M, D, A;
    }
}
