package cn.asany.shanhai.core.support.model.types;

import cn.asany.shanhai.core.domain.ModelFieldMetadata;
import cn.asany.shanhai.core.support.model.FieldType;
import cn.asany.storage.api.FileObject;
import lombok.Data;
import org.jfantasy.framework.dao.MatchType;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.stereotype.Component;

/**
 * 文件
 *
 * @author limaofeng
 */
@Data
@Component
public class AssetField implements FieldType<String, FileObject> {
  private String id = "File";
  private String name = "文件对象";
  private String javaType = FileObject.class.getName();
  private String graphQLType = "File";

  private String description;

  private FieldTypeFamily family;

  public AssetField() {}

  public AssetField(FieldTypeFamily family, String id, String name, String description) {
    this.id = id;
    this.name = name;
    this.family = family;
    this.description = description;
  }

  @Override
  public String getJavaType(ModelFieldMetadata metadata) {
    return this.javaType;
  }

  @Override
  public String getGraphQLType() {
    return graphQLType;
  }

  @Override
  public MatchType[] filters() {
    return new MatchType[] {};
  }
}
