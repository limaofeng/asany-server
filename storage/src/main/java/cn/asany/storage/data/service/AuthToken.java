package cn.asany.storage.data.service;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthToken {
  private String path;
  private Long user;
  private String personalToken;
}
