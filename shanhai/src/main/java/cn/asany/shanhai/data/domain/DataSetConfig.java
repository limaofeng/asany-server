package cn.asany.shanhai.data.domain;

import cn.asany.shanhai.data.domain.converter.DataSetFieldArrayConverter;
import cn.asany.shanhai.data.domain.converter.DataSetFilterArrayConverter;
import cn.asany.shanhai.data.domain.toy.DataSetField;
import cn.asany.shanhai.data.domain.toy.DataSetFilter;
import cn.asany.shanhai.data.engine.IDataSetOptions;
import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.jackson.JSON;

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
  @Column(name = "NAME", length = 50)
  private String name;
  /** 对应的数据源 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "DATASOURCE_ID",
      foreignKey = @ForeignKey(name = "FK_DATASET_CONFIG_DATASOURCE"),
      updatable = false,
      nullable = false)
  @ToString.Exclude
  private DataSourceConfig datasource;
  /** 存放配置参数 */
  @Column(name = "CONFIG_STORE", columnDefinition = "JSON")
  private String details;
  /** 字段列表 */
  @Column(name = "FIELDS_STORE", columnDefinition = "JSON")
  @Convert(converter = DataSetFieldArrayConverter.class)
  private List<DataSetField> fields;
  /** 过滤筛选 */
  @Column(name = "FILTERS_STORE", columnDefinition = "JSON")
  @Convert(converter = DataSetFilterArrayConverter.class)
  private List<DataSetFilter> filters;

  @Transient
  public <T extends IDataSetOptions> T getOptions(Class<T> optionsClass) {
    return JSON.deserialize(this.getDetails(), optionsClass);
  }

  public static class DataSetConfigBuilder {
    public DataSetConfigBuilder options(IDataSetOptions options) {
      this.details = JSON.serialize(options);
      return this;
    }
  }
}
