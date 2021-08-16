package cn.asany.ui.resources;

import java.util.List;

public interface UIResource {

  Long getId();

  List<String> getTags();

  void setTags(List<String> tags);
}
