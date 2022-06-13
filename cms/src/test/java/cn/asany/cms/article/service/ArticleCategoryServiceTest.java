package cn.asany.cms.article.service;

import cn.asany.cms.TestApplication;
import cn.asany.cms.article.domain.ArticleCategory;
import cn.asany.cms.article.dto.CmsSetup;
import java.io.InputStream;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.yaml.snakeyaml.Yaml;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = TestApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ArticleCategoryServiceTest {

  @Autowired private ArticleCategoryService articleCategoryService;

  @Test
  void save() {
    InputStream inputStream = ClassLoader.getSystemResourceAsStream("website.yml");
    Yaml yaml = new Yaml();
    CmsSetup setup = yaml.loadAs(inputStream, CmsSetup.class);

    log.debug("Channels:" + setup.getChannels().size());

    List<ArticleCategory> channels = this.articleCategoryService.saveAll(setup.getChannels(), 638L);

    log.debug("Channels:" + channels.size());
  }
}
