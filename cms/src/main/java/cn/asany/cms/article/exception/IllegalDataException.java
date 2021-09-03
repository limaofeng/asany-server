package cn.asany.cms.article.exception;

import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.GraphqlErrorHelper;
import graphql.language.SourceLocation;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;

/** @Description @Author ChenWenJie @Data 2020/10/27 2:56 下午 */
@Data
public class IllegalDataException extends RuntimeException implements GraphQLError {
  private String code;
  /** 定义的返回数据 */
  private Map<String, Object> data = new HashMap<>();

  public IllegalDataException(String code, String message) {
    super(message);
    this.code = code;
  }

  @Override
  public List<SourceLocation> getLocations() {
    return null;
  }

  @Override
  public ErrorClassification getErrorType() {
    return null;
  }

  @Override
  public List<Object> getPath() {
    return null;
  }

  @Override
  public Map<String, Object> toSpecification() {
    Map<String, Object> result = GraphqlErrorHelper.toSpecification(this);
    result.put("code", this.code);
    result.put("timestamp", new Date());
    return result;
  }

  @Override
  public Map<String, Object> getExtensions() {
    return null;
  }
}
