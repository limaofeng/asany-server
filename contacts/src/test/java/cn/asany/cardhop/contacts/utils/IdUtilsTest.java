package cn.asany.cardhop.contacts.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class IdUtilsTest {

  @Test
  void toKey() {
    String id = IdUtils.toKey(100000L, "department", 400000L);
    IdUtils.IdKey key = IdUtils.parseKey(id);
    assertEquals(key.getBook(), 100000L);
    assertEquals(key.getNamespace(), "department");
    assertEquals(key.getGroup(), 400000L);
  }
}
