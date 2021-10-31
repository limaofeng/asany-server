package cn.asany.storage.data.graphql.scalar;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.data.service.FileService;
import cn.asany.storage.dto.SimpleFileObject;
import graphql.language.IntValue;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.regexp.RegexpConstant;
import org.jfantasy.framework.util.regexp.RegexpUtil;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author limaofeng
 * @version V1.0
 * @date 2020/3/7 7:44 下午
 */
@Slf4j
public class FileCoercing implements Coercing<FileObject, Object> {

  @Autowired private FileService fileService;

  @Override
  public Object serialize(Object input) throws CoercingSerializeException {
    return input;
  }

  @Override
  public FileObject parseValue(Object input) throws CoercingParseValueException {
    Long fileId = null;

    if (input instanceof IntValue) {
      fileId = ((IntValue) input).getValue().longValue();
    } else if (input instanceof StringValue) {
      String value = ((StringValue) input).getValue();
      if (StringUtil.isBlank(value)) {
        return null;
      }
      if (!RegexpUtil.isMatch(value, RegexpConstant.VALIDATOR_NUMBER)) {
        return new SimpleFileObject(value);
      }
      fileId = Long.valueOf(value);
    }

    if (fileId == null) {
      return null;
    }

    return fileService.findById(fileId).orElse(null);
  }

  @Override
  public FileObject parseLiteral(Object input) throws CoercingParseLiteralException {
    return parseValue(input);
  }
}
