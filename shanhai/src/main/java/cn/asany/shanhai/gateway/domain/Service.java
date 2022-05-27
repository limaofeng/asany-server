package cn.asany.shanhai.gateway.domain;

import cn.asany.shanhai.gateway.domain.enums.Protocol;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.net.URL;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 服务发现
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
@Entity
@Table(name = "SH_SERVICE")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Service extends BaseBusEntity {
  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 编码 */
  @Column(name = "CODE", length = 50, unique = true)
  private String code;
  /** 名称 */
  @Column(name = "NAME", length = 50)
  private String name;
  /** 描述 */
  @Column(name = "DESCRIPTION", length = 200)
  private String description;
  /** 名称 */
  @Enumerated(EnumType.STRING)
  @Column(name = "PROTOCOL", length = 50)
  private Protocol protocol;
  /** 地址 */
  @Column(name = "HOST", length = 100)
  private String host;
  /** 端口 */
  @Column(name = "PORT", length = 10)
  private Integer port;
  /** 路径 */
  @Column(name = "PATH", length = 50)
  private String path;

  @OneToOne(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.ALL})
  @LazyToOne(LazyToOneOption.NO_PROXY)
  @PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "SERVICE_ID")
  private ServiceSchema schema;

  @Transient
  public String getUrl() {
    return this.protocol + "://" + this.host + this.protocol.getPort(this.port) + this.path;
  }

  public static class ServiceBuilder {

    @SneakyThrows
    public ServiceBuilder url(String url) {
      URL urlInfo = new URL(url);
      this.protocol(Protocol.valueOf(urlInfo.getProtocol().toUpperCase()));
      this.path(urlInfo.getPath());
      this.host(urlInfo.getHost());
      this.port(urlInfo.getPort() == -1 ? urlInfo.getDefaultPort() : urlInfo.getPort());
      return this;
    }
  }
}
