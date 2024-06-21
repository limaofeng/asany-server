/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.storage.data.graphql.scalar;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.data.domain.FileDetail;
import cn.asany.storage.data.util.IdUtils;
import cn.asany.storage.dto.SimpleFileObject;
import graphql.GraphQLContext;
import graphql.execution.CoercedVariables;
import graphql.language.StringValue;
import graphql.language.Value;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.error.ValidationException;
import net.asany.jfantasy.framework.util.common.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.env.Environment;

/**
 * @author limaofeng
 * @version V1.0
 * @date 2022/7/28 9:12 9:12
 */
@Slf4j
public class FileCoercing implements Coercing<FileObject, Object> {

  protected final Environment environment;

  public FileCoercing(Environment environment) {
    this.environment = environment;
  }

  @Override
  public @Nullable Object serialize(
      @NotNull Object dataFetcherResult,
      @NotNull GraphQLContext graphQLContext,
      @NotNull Locale locale)
      throws CoercingSerializeException {
    if (!(dataFetcherResult instanceof SimpleFileObject fileObject)) {
      return dataFetcherResult;
    }
    if (StringUtil.isBlank(fileObject.getUrl())) {
      fileObject.setUrl(
          environment.getProperty("STORAGE_BASE_URL") + "/preview/" + fileObject.getId());
    }
    return dataFetcherResult;
  }

  @Override
  public @Nullable FileObject parseValue(
      @NotNull Object input, @NotNull GraphQLContext graphQLContext, @NotNull Locale locale)
      throws CoercingParseValueException {
    if (input instanceof StringValue) {
      String value = ((StringValue) input).getValue();
      IdUtils.FileKey fileKey = IdUtils.parseKey(value);
      FileDetail detail = fileKey.getFile();
      return SimpleFileObject.builder()
          .id(value)
          .directory(false)
          .name(detail.getName())
          .size(detail.getSize())
          .lastModified(detail.getLastModified())
          .path(detail.getPath())
          .url(environment.getProperty("STORAGE_BASE_URL") + "/preview/" + value)
          .build();
    } else if (input instanceof String) {
      IdUtils.FileKey fileKey = IdUtils.parseKey((String) input);
      FileDetail detail = fileKey.getFile();
      return SimpleFileObject.builder()
          .id((String) input)
          .directory(false)
          .name(detail.getName())
          .size(detail.getSize())
          .mimeType(detail.getMimeType())
          .lastModified(detail.getLastModified())
          .path(detail.getPath())
          .url(environment.getProperty("STORAGE_BASE_URL") + "/preview/" + input)
          .build();
    }
    throw new ValidationException(String.format("不支持的类型 %s", input.getClass()));
  }

  @Override
  public FileObject parseLiteral(
      @NotNull Value<?> input,
      @NotNull CoercedVariables variables,
      @NotNull GraphQLContext graphQLContext,
      @NotNull Locale locale)
      throws CoercingParseLiteralException {
    return parseValue(input, graphQLContext, locale);
  }
}
