package cn.asany.shanhai.gateway.util;

import cn.asany.shanhai.core.domain.enums.ModelType;

public enum GraphQLType {
  /** 标量 */
  Scalar(ModelType.SCALAR),
  /** 输入对象 */
  InputObject(ModelType.INPUT_OBJECT),
  /** 对象 */
  Object(ModelType.OBJECT),
  /** 枚举型 */
  Enum(ModelType.ENUM),
  /** 联合 */
  Union(ModelType.UNION),
  /** 接口 */
  Interface(ModelType.INTERFACE);

  private ModelType modelType;

  GraphQLType(ModelType modelType) {
    this.modelType = modelType;
  }

  public ModelType toModelType() {
    return this.modelType;
  }
}
