package cn.asany.cms.article.service;

import cn.asany.cms.TestApplication;
import cn.asany.cms.article.bean.ArticleChannel;
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
class ArticleChannelServiceTest {

  @Autowired private ArticleChannelService articleChannelService;

  @Test
  void save() {
    InputStream inputStream = ClassLoader.getSystemResourceAsStream("cms.yml");
    Yaml yaml = new Yaml();
    CmsSetup setup = yaml.loadAs(inputStream, CmsSetup.class);

    log.debug("Channels:" + setup.getChannels().size());

    List<ArticleChannel> channels = this.articleChannelService.saveAll(setup.getChannels());

    log.debug("Channels:" + channels.size());
  }
}
