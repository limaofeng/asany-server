package cn.asany.crm.core.graphql.input;

import cn.asany.base.common.graphql.input.AddressInput;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerStoreCreateInput {
  /** 客户ID */
  private Long customer;
  /** 门店编号 */
  private String no;
  /** 门店名称 */
  private String name;
  /** 开业时间 */
  private Date openingDate;
  /** 门店电话 */
  private String phone;
  /** 门店地址 */
  private AddressInput address;
}
