package cn.asany.storage.data.graphql.scalar;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.data.service.FileService;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author limaofeng
 * @version V1.0
 * @date 2020/3/7 7:44 下午
 */
@Slf4j
public class FileObjectCoercing implements Coercing<FileObject, Object> {

  @Autowired private FileService fileService;

  public FileObjectCoercing() {}

  public FileObjectCoercing(FileService fileService) {
    this.fileService = fileService;
  }

  @Override
  public Object serialize(Object input) throws CoercingSerializeException {
    if (input instanceof FileObject) {
      return (FileObject) input;
    }
    return input;
  }

  @Override
  public FileObject parseValue(Object input) throws CoercingParseValueException {
    String fileId = null;
    if (input instanceof String) {
      fileId = input.toString();
    }

    if (input instanceof StringValue) {
      fileId = ((StringValue) input).getValue();
    }

    if (fileId == null) {
      return null;
    }
    return fileService.findById(Long.valueOf(fileId)).orElse(null);
  }

  @Override
  public FileObject parseLiteral(Object input) throws CoercingParseLiteralException {
    return parseValue(input);
  }
}
