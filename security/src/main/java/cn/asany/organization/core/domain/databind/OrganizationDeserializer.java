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
package cn.asany.organization.core.domain.databind;

import cn.asany.organization.core.domain.Organization;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import net.asany.jfantasy.framework.util.common.StringUtil;

/**
 * @author limaofeng
 */
public class OrganizationDeserializer extends JsonDeserializer<Organization> {

  @Override
  public Organization deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
    if (StringUtil.isBlank(jp.getValueAsString())) {
      return null;
    }
    return Organization.builder().id(jp.getValueAsLong()).build();
  }
}
