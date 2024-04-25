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
package cn.asany.cms.validators;

import net.asany.jfantasy.framework.spring.validation.ValidationException;
import net.asany.jfantasy.framework.spring.validation.Validator;
import net.asany.jfantasy.framework.util.common.StringUtil;
import org.springframework.stereotype.Component;

/**
 * @author limaofeng
 */
@Component
public class UrlUniqueValidator implements Validator<String> {

  //    private final ArticleService cmsService;
  //
  //    @Autowired
  //    public UrlUniqueValidator(ArticleService cmsService) {
  //        thisService = cmsService;
  //    }
  //
  @Override
  public void validate(String value) throws ValidationException {
    if (StringUtil.isBlank(value)) {
      return;
    }
    //        if (cmsService.findUniqueByUrl(value).isPresent()) {
    //            throw new ValidationException("编码[" + value + "]已经存在");
    //        }
    //    }

  }
}
