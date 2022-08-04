package cn.asany.im.auth.graphql.type;

import lombok.Getter;

@Getter
public enum Platform {
  iOS(1),
  Android(2),
  Windows(3),
  OSX(4),
  Web(5),
  MiniWeb(6),
  Linux(7),
  Admin(8);

  private final int value;

  Platform(int value) {
    this.value = value;
  }
}
