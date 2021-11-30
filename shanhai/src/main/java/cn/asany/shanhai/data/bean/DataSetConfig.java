package cn.asany.shanhai.data.bean;

import cn.asany.shanhai.data.bean.toy.DataSetField;
import cn.asany.shanhai.data.bean.toy.DataSetFilter;
import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 数据集
 *
 * @author limaofeng
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "DATA_SET_CONFIG")
public class DataSetConfig extends BaseBusEntity {
  @Id
  @Column(name = "ID", precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 名称 */
  @Column(name = "NAME")
  private String name;
  /** 对应的数据源 */
  private DataSourceConfig datasource;

  @Transient private List<DataSetField> fields;
  @Transient private List<DataSetFilter> filters;
}
