package cn.asany.cms.article.service;

import static org.junit.jupiter.api.Assertions.*;

import cn.asany.cms.TestApplication;
import cn.asany.cms.article.bean.Banner;
import cn.asany.cms.article.converter.BannerConverter;
import cn.asany.cms.article.graphql.input.BannerCreateInput;
import cn.asany.cms.article.graphql.input.BannerFilter;
import cn.asany.cms.article.graphql.input.BannerUpdateInput;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.jackson.JSON;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = TestApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class BannerServiceTest {

  @Autowired private BannerService service;

  @Autowired private BannerConverter converter;

  private final Set<Long> ids = new HashSet<>();

  @SneakyThrows
  @BeforeEach
  void init() {
    InputStream inputStream = ClassLoader.getSystemResourceAsStream("banners.json");
    List<BannerCreateInput> banners =
        JSON.getObjectMapper()
            .readValue(inputStream, new TypeReference<List<BannerCreateInput>>() {});
    for (BannerCreateInput input : banners) {
      Banner banner = this.service.save(converter.toBannerFromCreateInput(input));
      this.ids.add(banner.getId());
    }
  }

  @AfterEach
  void clear() {
    this.service.delete(this.ids);
  }

  @Test
  void findAll() {
    BannerFilter filter = new BannerFilter();
    List<Banner> banners = this.service.findAll(filter.build(), OrderBy.unsorted());
    assertNotNull(banners);
  }

  private Banner findOne() {
    Optional<Long> optionalId = this.ids.stream().findFirst();
    assertTrue(optionalId.isPresent());
    Optional<Banner> banner = this.service.findById(optionalId.get());
    assertTrue(banner.isPresent());
    return banner.get();
  }

  @Test
  void updateByMerge() {
    Banner banner = findOne();
    BannerUpdateInput input = new BannerUpdateInput();
    input.setButtonText("创建账户");
    banner = this.service.update(banner.getId(), converter.toBannerFromUpdateInput(input), true);
    assertEquals(banner.getButtonText(), "创建账户");
  }

  @Test
  void update() {
    Banner banner = findOne();
    BannerUpdateInput input = new BannerUpdateInput();
    input.setButtonText("创建账户");
    banner = this.service.update(banner.getId(), converter.toBannerFromUpdateInput(input), false);
    assertEquals(banner.getButtonText(), "创建账户");
  }
}
