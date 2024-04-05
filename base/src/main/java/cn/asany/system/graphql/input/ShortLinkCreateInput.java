package cn.asany.system.graphql.input;

import java.util.Date;
import java.util.Map;
import lombok.Data;

@Data
public class ShortLinkCreateInput {
  private String url;
  private String category;
  private Date expiresAt;
  private Map<String, String> metadata;
}
