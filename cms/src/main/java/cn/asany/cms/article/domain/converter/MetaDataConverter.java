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
package cn.asany.cms.article.domain.converter;

import cn.asany.cms.article.domain.ArticleMetadata;
import jakarta.persistence.AttributeConverter;
import net.asany.jfantasy.framework.jackson.JSON;
import net.asany.jfantasy.framework.util.common.StringUtil;

public class MetaDataConverter implements AttributeConverter<ArticleMetadata, String> {
  @Override
  public String convertToDatabaseColumn(ArticleMetadata attribute) {
    if (attribute == null) {
      return null;
    }
    return JSON.serialize(attribute);
  }

  @Override
  public ArticleMetadata convertToEntityAttribute(String dbData) {
    if (StringUtil.isBlank(dbData)) {
      return new ArticleMetadata();
    }
    return JSON.deserialize(dbData, ArticleMetadata.class);
  }
}
