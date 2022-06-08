package cn.asany.uptime.domain;

import cn.asany.organization.core.domain.Organization;
import cn.asany.uptime.domain.enums.MonitorType;
import java.util.List;

public class Monitor {
  private Long id;
  private String name;
  private String url;
  private String method;
  private String hostname;
  private Integer port;
  private String maxretries;
  private String weight;
  private Boolean active;
  private MonitorType type;
  private String interval;
  private String retryInterval;
  private String keyword;
  private String expiryNotification;
  private String ignoreTls;
  private String maxredirects;
  private String accepted_statuscodes;
  private String dns_resolve_type;
  private String dns_resolve_server;
  private String dns_last_result;
  private String proxyId;
  private List<Tag> tags;
  private Organization organization;
}
