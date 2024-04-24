package cn.asany.pim.core.graphql.input;

import cn.asany.base.common.graphql.input.AddressInput;
import cn.asany.base.common.graphql.input.ContactInformationInput;
import java.util.Date;
import lombok.Data;

@Data
public class DeviceUpdateInput {
  private String no;

  /** 设备序列号 */
  private String sn;

  /** 设备名称 */
  private String name;

  /** 购买日期 */
  private Date purchaseDate;

  /** 设备保修政策 */
  private Long warrantyPolicyId;

  /** 设备保修开始日期 */
  private Date warrantyStartDate;

  /** 设备保修结束日期 */
  private Date warrantyEndDate;

  /** 设备位置 */
  private AddressInput address;

  /** 设备联系信息 */
  private ContactInformationInput contactInfo;

  /** 设备所有者 */
  private DeviceOwnerInput owner;
}
