package cn.asany.cms.article.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 收藏类型表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2016-07-31 下午4:52:07
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ORG_STAR_TYPE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class StarType extends BaseBusEntity {

  @Id
  @Column(name = "ID", precision = 25)
  private String id;

  @Column(name = "NAME", length = 50)
  private String name;

  @Column(name = "VALUE_TYPE", length = 50)
  private String valueType;
}
