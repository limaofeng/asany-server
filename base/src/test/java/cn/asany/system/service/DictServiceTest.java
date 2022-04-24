package cn.asany.system.service;

import cn.asany.system.TestApplication;
import cn.asany.system.bean.Dict;
import cn.asany.system.bean.DictType;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.file.FileUtil;
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
class DictServiceTest {

  private static final String url =
      "https://raw.githubusercontent.com/modood/Administrative-divisions-of-China/master/dist/pcas-code.json";

  @Autowired private DictService dictService;

  @Test
  void saveDictType() {
    DictType pcas =
        dictService.save(DictType.builder().id("pcas").name("“省份、城市、区县、乡镇” 四级联动数据").build());

    DictType province =
        dictService.save(DictType.builder().id("province").name("省份").parent(pcas).build());

    DictType city = dictService.save(DictType.builder().id("city").name("城市").parent(pcas).build());

    DictType district =
        dictService.save(DictType.builder().id("district").name("区县").parent(pcas).build());

    DictType street =
        dictService.save(DictType.builder().id("street").name("乡镇/街道").parent(pcas).build());
  }

  @Test
  void importPcas() {
    // InputStream input = new URL(url).openStream();

    String body =
        FileUtil.readFile(
            "/Users/limaofeng/Workspace/whir/kuafu/base/src/test/resources/pcas-code.json");

    List<Dict> dicts = JSON.deserialize(body, new TypeReference<List<Dict>>() {});

    String[] codes = new String[] {"province", "city", "district", "street"};

    String[] municipality = new String[] {"11", "12", "31", "50"};

    dicts =
        ObjectUtil.recursive(
            dicts,
            (item, context) -> {
              Dict parent = context.getParent();
              if (parent != null) {
                item.setLevel(parent.getLevel() + 1);
                item.setType(codes[item.getLevel() - 1]);
              } else {
                item.setLevel(context.getLevel());
                item.setType(codes[context.getLevel() - 1]);
              }

              if (Arrays.stream(municipality).anyMatch(id -> id.equals(item.getCode()))) {
                List<Dict> children =
                    item.getChildren().stream()
                        .reduce(
                            new ArrayList<>(),
                            (acc, _item) -> {
                              acc.addAll(_item.getChildren());
                              return acc;
                            },
                            (acc, bcc) -> {
                              acc.addAll(bcc);
                              return acc;
                            });
                item.setLevel(context.getLevel() + 1);
                item.setType(codes[item.getLevel() - 1]);
                item.setChildren(children);
              }

              return item;
            });

    int rows = dictService.deleteDictByType("pcas");

    log.debug("删除 " + rows + " 条记录");

    dictService.saveAll(dicts);
  }
}
