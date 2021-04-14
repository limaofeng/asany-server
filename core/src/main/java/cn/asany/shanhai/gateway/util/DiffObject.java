package cn.asany.shanhai.gateway.util;

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

    public static enum DiffStatus {
        M, D, A;
    }
}
