package cn.asany.crm.support.graphql.input;

import cn.asany.base.common.graphql.input.ContactInformationInput;
import cn.asany.crm.support.domain.enums.TicketPriority;
import cn.asany.storage.api.FileObject;
import java.util.List;
import lombok.Data;

@Data
public class TicketCreateInput {
  /** 工单描述 */
  private String description;
  /** 附件 */
  private List<FileObject> attachments;
  /** 工单类型 */
  private Long type;
  /** 工单优先级 */
  private TicketPriority priority;
  /** 客户ID */
  private Long customerId;
  /** 门店ID */
  private Long storeId;
  /** 联系方式 */
  private ContactInformationInput contactInfo;
  /** 工单目标 */
  private TicketTargetInput target;
}
