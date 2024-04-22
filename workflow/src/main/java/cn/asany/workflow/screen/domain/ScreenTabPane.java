package cn.asany.workflow.screen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * @author limaofeng@msn.com @ClassName: PageField @Description: 页面域(这里用一句话描述这个类的作用)
 * @date 2022/7/28 9:12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "FSM_SCREEN_TAB_PANE")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class ScreenTabPane extends BaseBusEntity {

  @Id
  @Column(name = "ID", length = 22)
  private Long id;

  /** 名称 */
  @Column(name = "NAME", length = 50)
  private String name;

  /** 页面 */
  @JoinColumn(name = "SCREEN", foreignKey = @ForeignKey(name = "FK_SCREEN_TAB_PANE_SCREEN"))
  @ManyToOne(fetch = FetchType.LAZY)
  private Screen screen;

  /** 该tabPane包含的字段 */
  @OneToMany(
      mappedBy = "tabPane",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  private List<FieldToScreen> fields;
}
