package cn.asany.workflow.screen.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * @author penghanying @ClassName: PageField @Description: 页面域(这里用一句话描述这个类的作用)
 * @date 2019/5/22
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
