package cn.asany.pim.core.graphql.input;

import java.util.Date;
import lombok.Data;

@Data
public class DeviceCreateInput {
  /** 设备序列号 */
  private String sn;
  /** 设备名称 */
  private String name;
  /** 设备型号 */
  private Long productId;
  /** 设备保修政策 */
  private Long warrantyPolicyId;
  /** 设备保修开始日期 */
  private Date warrantyStartDate;
  /** 设备保修结束日期 */
  private Date warrantyEndDate;
  /** 设备所有者 */
  private DeviceOwnerInput owner;
}
