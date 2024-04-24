package cn.asany.website.landing.service;

import static org.junit.jupiter.api.Assertions.assertFalse;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.UploadOptions;
import cn.asany.storage.api.UploadService;
import cn.asany.storage.core.engine.virtual.VirtualFileObject;
import cn.asany.storage.data.util.IdUtils;
import cn.asany.storage.dto.SimpleFileObject;
import cn.asany.storage.utils.UploadUtils;
import cn.asany.website.TestApplication;
import cn.asany.website.landing.domain.LandingStore;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.util.common.ClassUtil;
import net.asany.jfantasy.framework.util.ognl.OgnlUtil;
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
class LandingServiceTest {

  @Autowired private LandingService landingService;
  @Autowired private UploadService uploadService;

  @Test
  void saveStoreAllInBath() {
    String EXCEL_FILE_PATH = "/Users/limaofeng/Downloads/H5门店及二维码/";
    File qrcodeFolder = new File(EXCEL_FILE_PATH + "H5-门店企业微信活码及清单20220308");
    Map<String, String> fields = new HashMap<>();
    fields.put("店号", "code");
    fields.put("门店名称", "name");
    fields.put("店铺地址", "address.detailedAddress");

    EasyExcel.read(
            EXCEL_FILE_PATH + "WMF门店清单-20220308.xlsx",
            new DemoDataListener<>(
                LandingStore.class,
                fields,
                (items) -> {
                  for (LandingStore store : items) {
                    File[] files =
                        qrcodeFolder.listFiles(item -> item.getName().startsWith(store.getCode()));
                    assert files != null;
                    if (files.length == 0) {
                      log.error("未发现二维码");
                      continue;
                    }
                    if (files.length > 1) {
                      log.warn(
                          store.getName()
                              + "二维码有多个,"
                              + Arrays.stream(files)
                                  .map(File::getName)
                                  .collect(Collectors.joining(",")));
                    }
                    UploadOptions options = UploadOptions.builder().space("orX8kLOV").build();
                    VirtualFileObject uploadFile =
                        (VirtualFileObject)
                            uploadService.upload(UploadUtils.fileToObject(files[0]), options);
                    FileObject qrCode =
                        SimpleFileObject.builder()
                            .id(IdUtils.toKey("space", "orX8kLOV", uploadFile.getId()))
                            .directory(false)
                            .name(uploadFile.getName())
                            .size(uploadFile.getSize())
                            .lastModified(uploadFile.getLastModified())
                            .path(uploadFile.getOriginalPath())
                            .build();
                    store.setQrCode(qrCode);
                  }

                  items = landingService.saveStoreAllInBath(items);
                  assertFalse(items.isEmpty());
                }))
        .sheet()
        .headRowNumber(2)
        .doRead();
  }

  public interface BatchSaver<T> {
    void apply(List<T> items) throws Exception;
  }

  private static class DemoDataListener<T> implements ReadListener<LinkedHashMap<Integer, Object>> {
    /** 单次缓存的数据量 */
    public static final int BATCH_COUNT = 100;

    private final Class<T> entityClass;

    private final Map<Integer, String> headMap = new HashMap<>();
    private final Map<String, String> fields;
    private final BatchSaver<T> saver;

    DemoDataListener(Class<T> entityClass, Map<String, String> fields, BatchSaver<T> saver) {
      this.entityClass = entityClass;
      this.fields = fields;
      this.saver = saver;
    }

    /** 临时存储 */
    private final List<T> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    @Override
    public void invoke(LinkedHashMap<Integer, Object> data, AnalysisContext context) {
      T bean = ClassUtil.newInstance(this.entityClass);

      OgnlUtil ognlUtil = OgnlUtil.getInstance();

      for (Map.Entry<Integer, Object> entry : data.entrySet()) {
        ognlUtil.setValue(headMap.get(entry.getKey()), bean, entry.getValue());
      }

      cachedDataList.add(bean);
      if (cachedDataList.size() >= BATCH_COUNT) {
        saveData();
        cachedDataList.clear();
      }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
      saveData();
    }

    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
      for (Map.Entry<Integer, ReadCellData<?>> entry : headMap.entrySet()) {
        this.headMap.put(entry.getKey(), fields.get(entry.getValue().getStringValue()));
      }
    }

    @SneakyThrows
    private void saveData() {
      saver.apply(cachedDataList);
      log.info("{}条数据，开始存储数据库！", cachedDataList.size());
      log.info("存储数据库成功！");
    }
  }
}
