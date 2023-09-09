package cn.asany.weixin.domain;

import cn.asany.weixin.framework.message.user.Sex;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Objects;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 微信用户基本信息
 *
 * @author 李茂峰
 */
@Getter
@Setter
@Builder
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(UserKey.class)
@Table(
    name = "WEIXIN_USER",
    uniqueConstraints =
        @UniqueConstraint(
            name = "UK_WEIXIN_USER_KEY",
            columnNames = {"APPID", "OPENID"}))
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
public class Fans extends BaseBusEntity {

  @Id private String appId;
  /** 用户的标识，对当前公众号唯一 */
  @Id private String openId;
  /** 用户的昵称 */
  @Column(name = "NICKNAME", length = 50)
  private String nickname;
  /** 用户的性别 */
  @Enumerated(EnumType.STRING)
  @Column(name = "SEX", length = 10)
  private Sex sex;
  /** 用户所在城市 */
  @Column(name = "CITY", length = 50)
  private String city;
  /** 用户所在国家 */
  @Column(name = "COUNTRY", length = 50)
  private String country;
  /** 用户所在省份 */
  @Column(name = "PROVINCE", length = 50)
  private String province;
  /** 用户的语言，简体中文为zh_CN */
  @Column(name = "LANGUAGE", length = 20)
  private String language;
  /** 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空 */
  @Column(name = "AVATAR", length = 500)
  private String avatar;
  /** 用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间 */
  @Column(name = "SUBSCRIBE_TIME")
  private Long subscribeTime;
  /** 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。 */
  @Column(name = "SUBSCRIBE")
  private Boolean subscribe;
  /** 唯一ID */
  @Column(name = "UNION_ID", length = 100)
  private String unionId;
  /** 最后消息时间 */
  @Column(name = "LAST_MESSAGE_TIME")
  private Long lastMessageTime;
  /** 最后查看消息时间 */
  @Column(name = "LAST_LOOK_TIME")
  private Long lastLookTime;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Fans fans = (Fans) o;
    return appId != null
        && Objects.equals(appId, fans.appId)
        && openId != null
        && Objects.equals(openId, fans.openId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(appId, openId);
  }
}
