package cn.asany.base.utils;

public class Base62 {
  private static final String BASE62 =
      "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

  public static String encode(long value) {
    StringBuilder sb = new StringBuilder();
    do {
      int i = (int) (value % 62);
      sb.append(BASE62.charAt(i));
      value /= 62;
    } while (value > 0);
    return sb.reverse().toString();
  }
}
