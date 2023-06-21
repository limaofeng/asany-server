package cn.asany.flowable.core.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 个人任务
 *
 * @author limaofeng
 */
@Data
@Builder
public class PersonalTask {
    private String name;
    private String description;
    private Integer priority;
    private Date dueDate;
}
