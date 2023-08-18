package cn.asany.system.service;

import cn.asany.system.TestApplication;
import cn.asany.system.domain.Dict;
import cn.asany.system.domain.DictType;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
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
  void importPcas() throws IOException {
    // InputStream input = new URL(url).openStream();

    String body =
        FileUtil.readString(
            Paths.get(
                "/Users/limaofeng/Workspace/whir/kuafu/base/src/test/resources/pcas-code.json"));

    List<Dict> dicts = JSON.deserialize(body, new TypeReference<List<Dict>>() {});

    String[] codes = new String[] {"province", "city", "district", "street"};

    String[] municipality = new String[] {"11", "12", "31", "50"};

    String countyLevelCity = "省直辖县级行政区划";

    dicts =
        ObjectUtil.recursive(
            dicts,
            (item, context) -> {
              Dict parent = context.getParent();
              if (item.getType() == null) {
                if (parent != null) {
                  item.setLevel(parent.getLevel() + 1);
                  item.setType(codes[item.getLevel() - 1]);
                } else {
                  item.setLevel(context.getLevel());
                  item.setType(codes[context.getLevel() - 1]);
                }
              }

              if (countyLevelCity.equals(item.getName())) {
                assert parent != null;
                parent
                    .getChildren()
                    .addAll(
                        item.getChildren().stream()
                            .peek(
                                _item -> {
                                  _item.setType("district");
                                  _item.setLevel(3);
                                })
                            .collect(Collectors.toList()));
                return null;
              } else if (Arrays.stream(municipality).anyMatch(id -> id.equals(item.getCode()))) {
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
                item.setCode(item.getCode() + "0");
                item.setLevel(context.getLevel() + 1);
                item.setType(codes[item.getLevel() - 1]);
                item.setChildren(children);
              }

              return item;
            });

    int rows = dictService.deleteDictByType("pcas");

    log.debug("删除 " + rows + " 条记录");

    List<Dict> all = dictService.saveAll(dicts);

    log.debug("保存 " + all.size() + " 条记录");
  }
}
