package cn.asany.shanhai.gateway.domain.enums;

/** @author limaofeng */
public enum Protocol {
  HTTP("http", 80),
  HTTPS("https", 443);

  private String protocol;
  private int defaultPort;

  Protocol(String protocol, int defaultPort) {
    this.protocol = protocol;
    this.defaultPort = defaultPort;
  }

  @Override
  public String toString() {
    return this.protocol;
  }

  public String getPort(Integer port) {
    if (defaultPort == port) {
      return "";
    }
    return ":" + port;
  }
}
