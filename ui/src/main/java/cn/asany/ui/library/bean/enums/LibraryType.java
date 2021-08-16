package cn.asany.ui.library.bean.enums;

public enum LibraryType {
  /** 图标 */
  ICONS("IconLibrary"),
  /** 设计系统 */
  DESIGN_SYSTEM("DesignSystem"),
  /** 组件 */
  COMPONENT("ComponentLibrary");

  private String name;

  LibraryType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
