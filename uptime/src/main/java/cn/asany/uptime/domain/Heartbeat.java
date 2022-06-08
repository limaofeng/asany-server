package cn.asany.uptime.domain;

import cn.asany.uptime.domain.enums.HeartbeatStatus;
import java.util.Date;

public class Heartbeat {
  private Long id;
  private HeartbeatStatus status;
  private Date time;
  private String msg;
  private String ping;
  private String important;
  private String duration;
}
