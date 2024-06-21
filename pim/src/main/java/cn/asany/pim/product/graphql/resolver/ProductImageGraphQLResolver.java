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
package cn.asany.pim.product.graphql.resolver;

import cn.asany.pim.product.domain.ProductImage;
import cn.asany.storage.dto.SimpleFileObject;
import graphql.kickstart.tools.GraphQLResolver;
import net.asany.jfantasy.framework.util.common.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ProductImageGraphQLResolver implements GraphQLResolver<ProductImage> {

  @Autowired protected Environment environment;

  public String url(ProductImage image) {
    if (StringUtil.isNotBlank(image.getUrl())) {
      return image.getUrl();
    }
    SimpleFileObject fileObject = ((SimpleFileObject) image.getImage());
    if (StringUtil.isBlank(fileObject.getUrl())) {
      fileObject.setUrl(
          environment.getProperty("STORAGE_BASE_URL") + "/preview/" + fileObject.getId());
    }
    return fileObject.getUrl();
  }
}
