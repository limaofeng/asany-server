package cn.asany.storage.data.util;

import static org.junit.jupiter.api.Assertions.*;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class IdUtilsTest {

  @Test
  void toKey() {
    log.info(IdUtils.toKey("space", "7VE4SSrk"));
  }
}
