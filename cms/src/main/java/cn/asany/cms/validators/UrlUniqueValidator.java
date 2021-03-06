package cn.asany.cms.validators;

import org.jfantasy.framework.spring.validation.ValidationException;
import org.jfantasy.framework.spring.validation.Validator;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.stereotype.Component;

/** @author limaofeng */
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
