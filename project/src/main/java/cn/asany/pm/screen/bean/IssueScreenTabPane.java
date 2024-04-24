package cn.asany.pm.screen.bean;

import cn.asany.pm.screen.bean.screenbind.ScreenDeserializer;
import cn.asany.pm.screen.bean.screenbind.ScreenSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

/**
 * @author limaofeng@msn.com @ClassName: PageField @Description: 页面域(这里用一句话描述这个类的作用)
 * @date 2022/7/28 9:12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ISSUE_SCREEN_TAB_PANE")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class IssueScreenTabPane extends BaseBusEntity {

  @Id
  @Column(name = "ID", length = 22)
  @TableGenerator
  private Long id;

  /** 名称 */
  @Column(name = "NAME", length = 50)
  private String name;

  /** 页面 */
  @JsonProperty("screen")
  @JsonSerialize(using = ScreenSerializer.class)
  @JsonDeserialize(using = ScreenDeserializer.class)
  @JoinColumn(name = "SID", foreignKey = @ForeignKey(name = "FK_GD_ISSUE_SCREEN_TAB_PANE_SID"))
  @ManyToOne(fetch = FetchType.LAZY)
  private IssueScreen issueScreen;

  /** 该tabPane包含的字段 */
  @OneToMany(
      mappedBy = "tabPane",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  private List<FieldToScreen> fields;
}
