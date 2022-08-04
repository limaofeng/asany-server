package cn.asany.im.auth.service.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ParseTokenData {
  @JsonProperty("ExpireTimeSeconds")
  private Long expireTimeSeconds;
}
