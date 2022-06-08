package cn.asany.uptime.domain;

import cn.asany.organization.core.domain.Organization;

public class Proxy {
  private Long id;
  private String protocol;
  private String host;
  private Integer port;
  private Boolean auth;
  private String username;
  private String password;
  private Boolean active;
  private Boolean _default;
  private Organization organization;
}
