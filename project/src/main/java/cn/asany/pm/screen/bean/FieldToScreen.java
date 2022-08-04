package cn.asany.pm.screen.bean;

import cn.asany.pm.field.bean.FieldConfigurationItem;
import cn.asany.pm.screen.bean.screenbind.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 将字段分配给页面
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "FIELD_SCREEN")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class FieldToScreen extends BaseBusEntity {

  @Id
  @Column(name = "ID", length = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  /** 页面 */
  @JsonProperty("screen")
  @JsonSerialize(using = ScreenSerializer.class)
  @JsonDeserialize(using = ScreenDeserializer.class)
  @JoinColumn(name = "SID", foreignKey = @ForeignKey(name = "FK_GD_FIELD_SCREEN_SID"))
  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private IssueScreen screen;

  /** 字段 */
  @JsonProperty("field")
  @JsonSerialize(using = IssueFieldSerializer.class)
  @JsonDeserialize(using = IssueFieldDeserializer.class)
  @JoinColumn(name = "FID", foreignKey = @ForeignKey(name = "FK_GD_FIELD_SCREEN_FID"))
  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private FieldConfigurationItem field;

  /** TabPane */
  @JsonProperty("tabPane")
  @JsonSerialize(using = DomainSerializer.class)
  @JsonDeserialize(using = DomainDeserializer.class)
  @JoinColumn(name = "TID", foreignKey = @ForeignKey(name = "FK_GD_FIDEL_TAB_PANE_TID"))
  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private IssueScreenTabPane tabPane;
  /** 排序字段 */
  @Column(name = "SORT", length = 22)
  private Long order;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    FieldToScreen that = (FieldToScreen) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
