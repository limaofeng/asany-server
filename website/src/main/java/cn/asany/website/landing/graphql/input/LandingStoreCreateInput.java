package cn.asany.website.landing.graphql.input;

import cn.asany.base.common.domain.Geolocation;
import cn.asany.base.common.graphql.input.AddressInput;
import cn.asany.storage.api.FileObject;
import lombok.Data;

@Data
public class LandingStoreCreateInput {
  private String code;
  private String name;
  private AddressInput address;
  private Geolocation location;
  private FileObject qrCode;
  private String leader;
  private String description;
}
